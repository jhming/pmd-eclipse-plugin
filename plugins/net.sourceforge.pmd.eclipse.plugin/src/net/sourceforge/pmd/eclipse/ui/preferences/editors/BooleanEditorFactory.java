package net.sourceforge.pmd.eclipse.ui.preferences.editors;

import net.sourceforge.pmd.PropertyDescriptor;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.eclipse.ui.preferences.br.SizeChangeListener;
import net.sourceforge.pmd.eclipse.ui.preferences.br.ValueChangeListener;
import net.sourceforge.pmd.lang.rule.properties.BooleanProperty;
import net.sourceforge.pmd.lang.rule.properties.PropertyDescriptorWrapper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Brian Remedios
 */
public class BooleanEditorFactory extends AbstractEditorFactory {

	public static final BooleanEditorFactory instance = new BooleanEditorFactory();


	private BooleanEditorFactory() { }

    public PropertyDescriptor<?> createDescriptor(String name, String description, Control[] otherData) {

        return new BooleanProperty(
                name,
                description,
                otherData == null ? Boolean.FALSE : valueFrom(otherData[1]),
                0
                );
    }

    private static BooleanProperty booleanPropertyFrom(PropertyDescriptor<?> desc) {

        if (desc instanceof PropertyDescriptorWrapper<?>) {
           return (BooleanProperty) ((PropertyDescriptorWrapper<?>)desc).getPropertyDescriptor();
           } else {
            return (BooleanProperty)desc;
         }
    }

    protected Boolean valueFrom(Control valueControl) {
        return ((Button)valueControl).getSelection() ? Boolean.TRUE : Boolean.FALSE;
    }

   public Control newEditorOn(Composite parent, final PropertyDescriptor<?> desc, final Rule rule, final ValueChangeListener listener, SizeChangeListener sizeListener) {

       final Button butt =  new Button(parent, SWT.CHECK);
       butt.setText("");

       final BooleanProperty bp = booleanPropertyFrom(desc);   // TODO - do I really have to do this?

       boolean set = ((Boolean)valueFor(rule, desc)).booleanValue();
       butt.setSelection(set);

       butt.addSelectionListener(new SelectionAdapter() {
           public void widgetSelected(SelectionEvent event) {
                boolean selected = butt.getSelection();
                if (selected == (((Boolean)valueFor(rule, bp))).booleanValue()) return;

                rule.setProperty(bp, Boolean.valueOf(selected));
                listener.changed(rule, desc, Boolean.valueOf(selected));
                adjustRendering(rule, desc, butt);
                }
        });

      return butt;
      }
}
