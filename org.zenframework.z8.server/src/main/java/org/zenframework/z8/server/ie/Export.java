package org.zenframework.z8.server.ie;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.zenframework.z8.ie.xml.ExportEntry;
import org.zenframework.z8.server.base.file.FileInfo;
import org.zenframework.z8.server.base.table.Table;
import org.zenframework.z8.server.base.table.TreeTable;
import org.zenframework.z8.server.base.table.value.AttachmentField;
import org.zenframework.z8.server.base.table.value.Field;
import org.zenframework.z8.server.db.generator.IForeignKey;
import org.zenframework.z8.server.logs.Trace;
import org.zenframework.z8.server.request.Loader;
import org.zenframework.z8.server.runtime.IObject;
import org.zenframework.z8.server.runtime.OBJECT;
import org.zenframework.z8.server.runtime.RCollection;
import org.zenframework.z8.server.runtime.RLinkedHashMap;
import org.zenframework.z8.server.types.bool;
import org.zenframework.z8.server.types.exception;
import org.zenframework.z8.server.types.guid;
import org.zenframework.z8.server.types.primary;
import org.zenframework.z8.server.types.string;
import org.zenframework.z8.server.types.sql.sql_bool;

public class Export extends OBJECT {

    private static final String NULL_PROTOCOL = "null";

    public static class CLASS<T extends Export> extends OBJECT.CLASS<T> {

        public CLASS() {
            this(null);
        }

        public CLASS(IObject container) {
            super(container);
            setJavaClass(Export.class);
            setName(Export.class.getName());
            setDisplayName(Export.class.getName());
        }

        @Override
        public Object newObject(IObject container) {
            return new Export(container);
        }

    }

    public final RLinkedHashMap<string, primary> properties = new RLinkedHashMap<string, primary>();

    protected final TransportContext.CLASS<TransportContext> context = new TransportContext.CLASS<TransportContext>();

    private final Map<String, ImportPolicy> policies = new HashMap<String, ImportPolicy>();
    private final List<RecordsetEntry> recordsetEntries = new LinkedList<RecordsetEntry>();
    private boolean exportAttachments = false;
    private String transportUrl;

    public Export(IObject container) {
        super(container);
    }

    @Override
    public void constructor2() {
        super.constructor2();
        z8_init();
    }

    public void addRecordset(Table table) {
        table.read(table.getPrimaryFields());
        recordsetEntries.add(new RecordsetEntry(table, table.getPrimaryFields()));
    }

    public void addRecordset(Table table, Collection<Field> fields) {
        table.read(fields);
        recordsetEntries.add(new RecordsetEntry(table, fields));
    }

    public void addRecordset(Table table, sql_bool where) {
        table.read(table.getPrimaryFields(), where);
        recordsetEntries.add(new RecordsetEntry(table, table.getPrimaryFields()));
    }

    public void addRecordset(Table table, Collection<Field> fields, sql_bool where) {
        table.read(fields, where);
        recordsetEntries.add(new RecordsetEntry(table, fields));
    }

    public void setPolicy(Table table, ImportPolicy policy) {
        policies.put(table.classId(), policy);
    }

    public void setPolicy(String tableClass, ImportPolicy policy) {
        policies.put(tableClass, policy);
    }

    public ImportPolicy getPolicy(Table table) {
        return getPolicy(table.classId());
    }

    public ImportPolicy getPolicy(String tableClass) {
        return policies.containsKey(tableClass) ? policies.get(tableClass) : ImportPolicy.DEFAULT;
    }

    public boolean isExportAttachments() {
        return exportAttachments;
    }

    public void setExportAttachments(boolean exportAttachments) {
        this.exportAttachments = exportAttachments;
    }

    public String getTransportUrl() {
        return transportUrl;
    }

    public void setTransportUrl(String transportUrl) {
        this.transportUrl = transportUrl;
    }

    public void execute() {
        RecordsSorter recordsSorter = new RecordsSorter();
        Message message = Message.instance();
        try {
            String protocol = getProtocol();
            if (!protocol.equals(NULL_PROTOCOL)) {
                // Если протокол НЕ "null", экспортировать записи БД
                for (RecordsetEntry recordsetEntry : recordsetEntries) {
                    while (recordsetEntry.recordset.next()) {
                        exportRecord(recordsetEntry, message, getPolicy(recordsetEntry.recordset.classId()), recordsSorter,
                                0);
                    }
                }
                // Сортировка записей в соответствии со ссылками по foreign keys и parentId
                Collections.sort(message.getExportEntry().getRecords().getRecord(), recordsSorter.getComparator());
                Trace.logEvent("Sorted records:");
                for (ExportEntry.Records.Record record : message.getExportEntry().getRecords().getRecord()) {
                    Trace.logEvent(record.getTable() + '[' + record.getRecordId() + ']');
                }
            }
            // Свойства экспортируются для любого протокола
            if (!properties.isEmpty()) {
                ExportEntry.Properties properties = new ExportEntry.Properties();
                for (Map.Entry<string, primary> entry : this.properties.entrySet()) {
                    ExportEntry.Properties.Property property = new ExportEntry.Properties.Property();
                    property.setKey(entry.getKey().get());
                    property.setValue(entry.getValue().toString());
                    property.setType(entry.getValue().type().toString());
                    properties.getProperty().add(property);
                }
                message.getExportEntry().setProperties(properties);
            }
            // Запись сообщения в таблицу ExportMessages
            message.setAddress(getAddress());
            message.setSender(context.get().getProperty(TransportContext.SelfAddressProperty));
            ExportMessages.instance().addMessage(message, protocol);
        } catch (JAXBException e) {
            throw new exception("Can't marshal records", e);
        }
    }

    public static URI getTransportUrl(String protocol, String address) throws URISyntaxException {
        return new URI(protocol + ":" + address);
    }

    public void z8_addRecordset(Table.CLASS<? extends Table> cls) {
        addRecordset(cls.get());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void z8_addRecordset(Table.CLASS<? extends Table> cls, RCollection fields) {
        addRecordset(cls.get(), CLASS.asList(fields));
    }

    public void z8_addRecordset(Table.CLASS<? extends Table> cls, sql_bool where) {
        addRecordset(cls.get(), where);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void z8_addRecordset(Table.CLASS<? extends Table> cls, RCollection fields, sql_bool where) {
        addRecordset(cls.get(), CLASS.asList(fields), where);
    }

    public void z8_setPolicy(Table.CLASS<? extends Table> cls, ImportPolicy policy) {
        setPolicy(cls.get(), policy);
    }

    public void z8_setExportAttachments(bool exportAttachments) {
        setExportAttachments(exportAttachments.get());
    }

    public void z8_setTransportUrl(string transportUrl) {
        setTransportUrl(transportUrl.get());
    }

    public void z8_init() {}

    public void z8_execute() {
        execute();
    }

    public static string z8_getTransportUrl(string protocol, string address) {
        try {
            return new string(getTransportUrl(protocol.get(), address.get()).toString());
        } catch (URISyntaxException e) {
            throw new exception(e.getMessage(), e);
        }
    }

    private void exportRecord(RecordsetEntry recordsetEntry, Message message, ImportPolicy policy,
            RecordsSorter recordsSorter, int level) {
        guid recordId = recordsetEntry.recordset.recordId();
        String table = recordsetEntry.recordset.classId();
        if (!recordsSorter.contains(table, recordId) && !IeUtil.isBuiltinRecord(recordsetEntry.recordset, recordId)) {
            if (policies.containsKey(recordsetEntry.recordset.classId())) {
                policy = policies.get(recordsetEntry.recordset.classId());
            }
            Trace.logEvent(getSpaces(level) + "Export record " + recordsetEntry.recordset.name() + '[' + recordId + ']');
            ExportEntry.Records.Record record = IeUtil.tableToRecord(recordsetEntry.recordset, recordsetEntry.fields,
                    policy == null ? null : policy.getSelfPolicy(), isExportAttachments());
            message.getExportEntry().getRecords().getRecord().add(record);
            recordsSorter.addRecord(table, recordId);
            // Вложения
            if (isExportAttachments()) {
                for (AttachmentField attField : recordsetEntry.recordset.getAttachments()) {
                    List<FileInfo> fileInfos = FileInfo.parseArray(attField.get().string().get());
                    message.getExportEntry().getFiles().getFile()
                            .addAll(IeUtil.fileInfosToFiles(fileInfos, policy == null ? null : policy.getSelfPolicy()));
                }
            }
            // Ссылки на другие таблицы
            // TODO Переделать через links
            for (IForeignKey fkey : recordsetEntry.recordset.getForeignKeys()) {
                if (fkey.getReferencedTable() instanceof Table) {
                    Trace.logEvent(getSpaces(level + 1) + fkey.getFieldDescriptor().name() + " --> "
                            + fkey.getReferencedTable().name() + '['
                            + recordsetEntry.recordset.getFieldByName(fkey.getFieldDescriptor().name()).guid().toString()
                            + ']');
                    Table refRecord = (Table) Loader.getInstance(((Table) fkey.getReferencedTable()).classId());
                    guid refGuid = recordsetEntry.recordset.getFieldByName(fkey.getFieldDescriptor().name()).guid();
                    if (refRecord.readRecord(refGuid, refRecord.getPrimaryFields())) {
                        exportRecord(new RecordsetEntry(refRecord, refRecord.getPrimaryFields()), message,
                                policy == null ? null : policy.getRelationsPolicy(), recordsSorter, level + 1);
                        recordsSorter.addLink(table, recordId, refRecord.classId(), refGuid);
                    }
                }
            }
            if (recordsetEntry.recordset instanceof TreeTable) {
                TreeTable treeRecordSet = (TreeTable) recordsetEntry.recordset;
                guid parentId = treeRecordSet.parentId.get().guid();
                if (!guid.NULL.equals(parentId)) {
                    TreeTable parentRecord = (TreeTable) Loader.getInstance(recordsetEntry.recordset.classId());
                    if (parentRecord.readRecord(parentId, parentRecord.getPrimaryFields())) {
                        exportRecord(new RecordsetEntry(parentRecord, parentRecord.getPrimaryFields()), message,
                                policy == null ? null : policy.getRelationsPolicy(), recordsSorter, level + 1);
                        recordsSorter.addLink(table, recordId, table, parentId);
                    }
                }
            }
        }
    }

    private String getProtocol() {
        int pos = transportUrl.indexOf(':');
        return pos > 0 ? transportUrl.substring(0, pos) : NULL_PROTOCOL;
    }

    private String getAddress() {
        int pos = transportUrl.indexOf(':');
        return pos > 0 ? transportUrl.substring(pos + 1) : null;
    }

    private static String getSpaces(int level) {
        char buf[] = new char[level * 4];
        Arrays.fill(buf, ' ');
        return new String(buf);
    }

    private static class RecordsetEntry {

        final Table recordset;
        final Collection<Field> fields;

        RecordsetEntry(Table recordset, Collection<Field> fields) {
            this.recordset = recordset;
            this.fields = fields;
        }

    }

}
