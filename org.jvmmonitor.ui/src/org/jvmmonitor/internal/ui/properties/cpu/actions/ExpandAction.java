/*******************************************************************************
 * Copyright (c) 2010 JVM Monitor project. All rights reserved. 
 * 
 * This code is distributed under the terms of the Eclipse Public License v1.0
 * which is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.jvmmonitor.internal.ui.properties.cpu.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.jvmmonitor.core.cpu.ICallTreeNode;
import org.jvmmonitor.core.cpu.ITreeNode;
import org.jvmmonitor.internal.ui.properties.cpu.AbstractFilteredTree;

/**
 * Supports automatic expansion of the call tree with one click.
 * 
 * @author John Amos (johnamos@stanfordalumni.org)
 */
public class ExpandAction extends Action {
  /**
   * The percentage of total time consumed by a node that is used as an
   * expansion threshold.
   */
  private static final double TOTAL_TIME_PERCENTAGE = 4.0;

  /** The filtered tree. */
  private AbstractFilteredTree filteredTree;

  public ExpandAction(AbstractFilteredTree filteredTree) {
    this.filteredTree = filteredTree;
    setText(Messages.expandLabel);
  }

  @Override
  public void run() {
    recursiveExpansion(((TreeSelection) filteredTree.getViewer().getSelection()).getFirstElement());
  }

  /**
   * Expands the supplied node and calls itself for children.
   * 
   * @param element
   */
  private void recursiveExpansion(Object element) {
    if (element instanceof ICallTreeNode) {
      ICallTreeNode callTreeNode = (ICallTreeNode) element;
      if (callTreeNode.hasChildren()) {
        for (ITreeNode child : callTreeNode.getChildren()) {
          if (child instanceof ICallTreeNode) {
            ICallTreeNode childNode = (ICallTreeNode) child;
            if (childNode.getTotalTimeInPercentage() > TOTAL_TIME_PERCENTAGE) {
              filteredTree.getViewer().expandToLevel(childNode, 1);
              recursiveExpansion(childNode);
            }
          }
        }
      }
    }
  }
}
