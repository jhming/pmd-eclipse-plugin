package net.sourceforge.pmd.eclipse.ui.preferences.br;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.eclipse.util.Util;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Brian Remedios
 */
public class RuleSetTreeItemProvider implements ITreeContentProvider {

	private RuleFieldAccessor 	fieldAccessor;
	private String 				groupDescription;
	private Comparator			comparator;
	/**
	 * Constructor for RuleSetTreeItemProvider.
	 * @param accessor RuleFieldAccessor
	 * @param description String
	 */
	public RuleSetTreeItemProvider(RuleFieldAccessor accessor, String description, Comparator<?> theComparator) {
		fieldAccessor = accessor;
		groupDescription = description;
		comparator = theComparator;
	}

	/**
	 *
	 * @param accessor RuleFieldAccessor
	 */
	public void accessor(RuleFieldAccessor accessor) {
		fieldAccessor = accessor;
	}

	private Object[] sort(Collection<Rule> ruleColl) {
		Object[] rules = ruleColl.toArray();
		if (comparator == null) return rules;

		Arrays.sort(rules, comparator);
		return rules;
	}

	/**
	 *
	 * @param parentElement Object
	 * @return Object[]
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(Object)
	 */
	public Object[] getChildren(Object parentElement) {

	     if (parentElement instanceof RuleSet) {
	        RuleSet ruleSet = (RuleSet) parentElement;
	        return fieldAccessor == null ?
	        	sort(ruleSet.getRules()) :
	        	asRuleGroups(ruleSet.getRules());
	        }

	     if (parentElement instanceof RuleGroup) {
	    	 RuleGroup ruleGroup = (RuleGroup) parentElement;
		     return ruleGroup.rules();
		     }

	     return Util.EMPTY_ARRAY;
	}

	/**
	 * Method asRuleGroups.
	 * @param rules Collection<Rule>
	 * @return RuleGroup[]
	 */
	private RuleGroup[] asRuleGroups(Collection<Rule> rules) {

		Iterator<Rule> iter = rules.iterator();
		Map<Object, RuleGroup> ruleGroups = new HashMap<Object, RuleGroup>();
		while (iter.hasNext()) {
			Rule rule = iter.next();

			Comparable<?> groupId = fieldAccessor.valueFor(rule);

			RuleGroup group = ruleGroups.get(groupId);
			if (group != null) {
				group.add(rule);
			} else {
				group = new RuleGroup(
				        groupId,
				        fieldAccessor.labelFor(rule),
				        groupDescription
				        );
				group.add(rule);
				ruleGroups.put(groupId, group);
				}
		}

        RuleGroup[] groups = ruleGroups.values().toArray(new RuleGroup[ruleGroups.size()]);

		// TODO sort within groups
		for (RuleGroup group : groups) group.sortBy(comparator);

		 Arrays.sort(groups);
		 return groups;
	}

	/**
	 * Method getParent.
	 * @param element Object
	 * @return Object
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(Object)
	 */
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method hasChildren.
	 * @param element Object
	 * @return boolean
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(Object)
	 */
	public boolean hasChildren(Object element) {

		if (element instanceof RuleSet) {
            RuleSet ruleSet = (RuleSet) element;
            return ruleSet.getRules().size() > 0;
		}

		if (element instanceof RuleGroup) {
			return ((RuleGroup)element).hasRules();
		}

        return false;
	}

	/**
	 * Method getElements.
	 * @param inputElement Object
	 * @return Object[]
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	/**
	 * Method inputChanged.
	 * @param viewer Viewer
	 * @param oldInput Object
	 * @param newInput Object
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	public void dispose() {
		// TODO Auto-generated method stub
	}

}
