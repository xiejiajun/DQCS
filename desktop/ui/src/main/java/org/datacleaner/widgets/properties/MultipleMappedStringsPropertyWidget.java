/**
 * DataCleaner (community edition)
 * Copyright (C) 2014 Free Software Foundation, Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.datacleaner.widgets.properties;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;

import org.datacleaner.api.InputColumn;
import org.datacleaner.descriptors.ConfiguredPropertyDescriptor;
import org.datacleaner.job.builder.ComponentBuilder;
import org.datacleaner.panels.DCPanel;
import org.datacleaner.util.DCDocumentListener;
import org.datacleaner.util.StringUtils;
import org.datacleaner.util.WidgetFactory;
import org.datacleaner.widgets.DCCheckBox;
import org.jdesktop.swingx.JXTextField;

/**
 * A specialized property widget for multiple input columns that are mapped to
 * string values. This widget looks like the
 * {@link MultipleInputColumnsPropertyWidget}, but is enhanced with string text
 * fields.
 * 一个专用的属性小部件，用于映射到字符串值的多个输入列。
 * 这个小部件看起来像{@link MultipleInputColumnsPropertyWidget}，但是通过字符串文本字段得到了增强。
 */
public class MultipleMappedStringsPropertyWidget extends MultipleInputColumnsPropertyWidget {

    public class MappedStringsPropertyWidget extends MinimalPropertyWidget<String[]> {

        public MappedStringsPropertyWidget(final ComponentBuilder componentBuilder,
                final ConfiguredPropertyDescriptor propertyDescriptor) {
            super(componentBuilder, propertyDescriptor);
        }

        @Override
        public JComponent getWidget() {
            // do not return a visual widget
            return null;
        }

        @Override
        public boolean isSet() {
            return MultipleMappedStringsPropertyWidget.this.isSet();
        }

        @Override
        public String[] getValue() {
            return getMappedStrings();
        }

        @Override
        protected void setValue(final String[] value) {
            if (MultipleMappedStringsPropertyWidget.this.isUpdating()) {
                return;
            }
            setMappedStrings(value);
        }
    }

    private final WeakHashMap<InputColumn<?>, JXTextField> _mappedTextFields;
    private final ConfiguredPropertyDescriptor _mappedStringsProperty;
    private final MappedStringsPropertyWidget _mappedStringsPropertyWidget;

    /**
     * Constructs the property widget
     *
     * @param componentBuilder
     *            the component builder for the table lookup
     * @param inputColumnsProperty
     *            the property representing the columns to use for setting up
     *            conditional lookup (InputColumn[])
     * @param mappedStringsProperty
     *            the property representing the mapped strings (String[])
     */
    public MultipleMappedStringsPropertyWidget(final ComponentBuilder componentBuilder,
            final ConfiguredPropertyDescriptor inputColumnsProperty,
            final ConfiguredPropertyDescriptor mappedStringsProperty) {
        super(componentBuilder, inputColumnsProperty);
        _mappedTextFields = new WeakHashMap<>();
        _mappedStringsProperty = mappedStringsProperty;

        _mappedStringsPropertyWidget = new MappedStringsPropertyWidget(componentBuilder, mappedStringsProperty);

        final InputColumn<?>[] currentValue = getCurrentValue();
        final String[] currentMappedStringsValue =
                (String[]) componentBuilder.getConfiguredProperty(mappedStringsProperty);
        if (currentValue != null && currentMappedStringsValue != null) {
            // first create combo's, then set value (so combo is ready before it
            // is requested)

            _mappedStringsPropertyWidget.setValue(currentMappedStringsValue);
            final int minLength = Math.min(currentValue.length, currentMappedStringsValue.length);
            for (int i = 0; i < minLength; i++) {
                final InputColumn<?> inputColumn = currentValue[i];
                final String mappedString = currentMappedStringsValue[i];
                createTextField(inputColumn, mappedString);
            }

            setValue(currentValue);
        }
    }

    @Override
    protected boolean isAllInputColumnsSelectedIfNoValueExist() {
        return false;
    }

    private JXTextField createTextField(final InputColumn<?> inputColumn, String mappedString) {
        final JXTextField textField = WidgetFactory.createTextField();
        _mappedTextFields.put(inputColumn, textField);

        if (mappedString == null) {
            mappedString = getDefaultMappedString(inputColumn);
        }
        if (mappedString != null) {
            textField.setText(mappedString);
        }
        textField.getDocument().addDocumentListener(new DCDocumentListener() {
            @Override
            protected void onChange(final DocumentEvent event) {
                if (isBatchUpdating()) {
                    return;
                }
                fireValueChanged();
                _mappedStringsPropertyWidget.fireValueChanged();
            }
        });
        return textField;
    }

    /**
     * Subclasses can override this method to set a default value for a column
     * when it is selected.
     *
     * @param inputColumn
     * @return
     */
    protected String getDefaultMappedString(final InputColumn<?> inputColumn) {
        return "";
    }

    @Override
    protected JComponent decorateCheckBox(final DCCheckBox<InputColumn<?>> checkBox) {
        final JXTextField textField;
        if (_mappedTextFields.containsKey(checkBox.getValue())) {
            textField = _mappedTextFields.get(checkBox.getValue());
        } else {
            textField = createTextField(checkBox.getValue(), null);
        }
        checkBox.addListenerToHead((item, selected) -> {
            textField.setVisible(selected);
            updateUI();
        });
        checkBox.addListener(new DCCheckBox.Listener<InputColumn<?>>() {
            @Override
            public void onItemSelected(InputColumn<?> item, boolean selected) {
                if (isBatchUpdating()) {
                    return;
                }
                _mappedStringsPropertyWidget.fireValueChanged();
            }
        });

        textField.setVisible(checkBox.isSelected());

        final DCPanel panel = new DCPanel();
        panel.setLayout(new BorderLayout());
        panel.add(checkBox, BorderLayout.CENTER);
        panel.add(textField, BorderLayout.EAST);
        return panel;
    }

    public ConfiguredPropertyDescriptor getMappedStringsProperty() {
        return _mappedStringsProperty;
    }

    public MappedStringsPropertyWidget getMappedStringsPropertyWidget() {
        return _mappedStringsPropertyWidget;
    }

    @Override
    public InputColumn<?>[] getValue() {
        final InputColumn<?>[] checkedInputColumns = super.getValue();
        final List<InputColumn<?>> result = new ArrayList<>();
        for (final InputColumn<?> inputColumn : checkedInputColumns) {
            // exclude input columns that have not been mapped yet
            final JXTextField textField = _mappedTextFields.get(inputColumn);
            if (textField != null && textField.isVisible()) {
                if (!StringUtils.isNullOrEmpty(textField.getText())) {
                    result.add(inputColumn);
                }
            }
        }
        return result.toArray(new InputColumn[result.size()]);
    }

    public String[] getMappedStrings() {
        final List<InputColumn<?>> inputColumns = MultipleMappedStringsPropertyWidget.this.getSelectedInputColumns();
        final List<String> result = new ArrayList<>();
        for (final InputColumn<?> inputColumn : inputColumns) {
            final JXTextField textField = _mappedTextFields.get(inputColumn);
            if (textField == null) {
                result.add(null);
            } else {
                final String value = textField.getText();
                result.add(value);
            }
        }

        return result.toArray(new String[result.size()]);
    }

    public void setMappedStrings(final String[] value) {
        final List<InputColumn<?>> inputColumns = MultipleMappedStringsPropertyWidget.this.getSelectedInputColumns();

        for (int i = 0; i < inputColumns.size(); i++) {
            final InputColumn<?> inputColumn = inputColumns.get(i);
            final String mappedString;
            if (value == null) {
                mappedString = getDefaultMappedString(inputColumn);
            } else if (i < value.length) {
                mappedString = value[i];
            } else {
                mappedString = getDefaultMappedString(inputColumn);
            }
            final JXTextField textField = _mappedTextFields.get(inputColumn);
            textField.setVisible(true);

            final String previousText = textField.getText();
            if (!mappedString.equals(previousText)) {
                textField.setText(mappedString);
            }
        }
    }

    @Override
    protected void onValuesBatchSelected(final List<InputColumn<?>> values) {
        for (final JXTextField textField : _mappedTextFields.values()) {
            textField.setVisible(false);
        }
        for (final InputColumn<?> inputColumn : values) {
            final JXTextField textField = _mappedTextFields.get(inputColumn);
            if (textField != null) {
                textField.setVisible(true);
            }
        }
    }

    @Override
    protected void onBatchFinished() {
        super.onBatchFinished();
        _mappedStringsPropertyWidget.fireValueChanged();
    }
}
