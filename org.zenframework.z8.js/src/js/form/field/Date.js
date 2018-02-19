Z8.define('Z8.form.field.Date', {
	extend: 'Z8.form.field.Text',

	triggers: [{ icon: 'fa-calendar' }],

	enterToOpen: true,
	enterOnce: false,

	format: 'd.m.Y',

	initComponent: function() {
		this.callParent();
	},

	setValue: function(value, dispayValue) {
		this.entered = true;

		value = String.isString(value) ? this.rawToValue(value) : value;
		this.callParent(value, dispayValue);
	},

	valueToRaw: function(value) {
		return Format.date(value, this.format);
	},

	rawToValue: function(value) {
		return Parser.datetime(value, this.format);
	},

	isEqual: function(value1, value2) {
		return Date.isEqual(value1, value2);
	},

	htmlMarkup: function() {
		var markup = this.callParent();

		var value = this.getValue();
		var dropdown = this.dropdown = new Z8.calendar.Dropdown({ date: value != null ? new Date(value) : null });
		dropdown.on('dayClick', this.setValue, this);
		dropdown.on('cancel', this.cancelDropdown, this);
		dropdown.on('show', this.onDropdownShow, this);
		dropdown.on('hide', this.onDropdownHide, this);

		markup.cn.push(dropdown.htmlMarkup());
		return markup;
	},

	subcomponents: function() {
		return this.callParent().add(this.dropdown);
	},

	completeRender: function() {
		this.callParent();
		this.dropdown.setAlignment(this.input);
	},

	select: function(direction) {
	},

	show: function() {
		this.entered = false;
		this.callParent();
	},

	showDropdown: function() {
		var dropdown = this.dropdown;

		var left = DOM.getOffsetLeft(this.input);
		DOM.setLeft(dropdown, left);

		this.dropdown.show();
	},

	hideDropdown: function() {
		this.dropdown.hide();
	},

	openDropdown: function() {
		var value = this.getValue();
		this.dropdown.set(value != null ? new Date(value) : null);
		this.showDropdown();
	},

	cancelDropdown: function() {
		this.hideDropdown();
	},

	toggleDropdown: function() {
		if(this.dropdown.visible)
			this.cancelDropdown();
		else
			this.openDropdown();
	},

	onDropdownShow: function() {
		this.getTrigger().rotateIcon(360);
	},

	onDropdownHide: function() {
		this.getTrigger().rotateIcon(0);
		DOM.focus(this.input);
	},

	onFocusOut: function(event) {
		if(!this.callParent(event))
			return false;

		this.cancelDropdown();
		return true;
	},

	onTriggerClick: function(trigger) {
		this.toggleDropdown();
	},

	onKeyEvent: function(event, target) {
		var key = event.getKey();

		var editable = this.editable;
		var dropdown = this.dropdown;
		var dropdownOpen = dropdown.visible;

		var me = this;
		var isTrigger = function() {
			return DOM.isParentOf(me.trigger, target);
		};

		var isInput = function() {
			return DOM.isParentOf(me.input, target);
		};

		if(key == Event.DOWN)
			dropdownOpen ? dropdown.focus() : this.select('next');
		else if(key == Event.UP)
			dropdownOpen ? dropdown.focus() : this.select('previous');
		else if(key == Event.TAB && isTrigger()) {
			dropdownOpen ? dropdown.focus() : this.cancelDropdown();
			return false;
		} else if(key == Event.ENTER || key == Event.SPACE && isTrigger()) {
			if(dropdownOpen) {
				this.setValue(dropdown.date);
				this.cancelDropdown();
			} else if(this.enterToOpen && (!this.enterOnce || !this.entered)) {
				this.onTriggerClick(event, target);
				this.entered = true;
			} else if(event.ctrlKey)
				this.onTriggerClick(event, target);
			else
				return false;
		} else if(key == Event.ESC && dropdownOpen)
			this.cancelDropdown();
		else
			return false;

		return true;
	}
});