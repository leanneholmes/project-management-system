package controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import access.ProjectManager;
import access.WorkPackageManager;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import models.WorkPackage;

/**
 * This class is the controller class
 * for managing the tree structure of 
 * work packages in the system.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("ttWorkPackageBean")
@SessionScoped
public class TreeTableWorkPackageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Class Manager. */
    @Inject
    private ProjectManager projectManager;

    /** Class Manager. */
    @Inject
    private WorkPackageManager workPackageManager;

    /** Controller. */
    @Inject
    private ProjectController projectController;

    /** Root Work Package Node. */
    private TreeNode<WorkPackage> root;

    /** Selected Work Package Node in Tree. */
    private TreeNode<WorkPackage> selectedNode;

    /** Map of states of work package nodes in tree. */
    private Map<String, Boolean> expandedStateMap;

    /** Default Constructor.
     *  Initializes work package nodes states map.
     */
    public TreeTableWorkPackageBean() {
        expandedStateMap = new HashMap<>();
    }

    /**
     * Initializes the work package tree and all of its nodes.
     */
    @PostConstruct
    public void init() {
        System.out.println("init called");
        System.out.println("getting first level WPs!");
        List<WorkPackage> firstLevelWorkPackages = workPackageManager.
                getAllFirstLevelWorkPackagesForProject(
                    projectController.getCurrentProject());
        System.out.println("size of first level WPs: " 
            + firstLevelWorkPackages.size());
        WorkPackage projectWorkPackage = new WorkPackage();
        projectWorkPackage.setId(
            projectController.getCurrentProject().getProjectId() + "");
        
        projectWorkPackage.setName(
            projectController.getCurrentProject().getProjectName()); 
        projectWorkPackage.setProject(projectController.getCurrentProject());
        projectWorkPackage.setPlaceholder(true);
        root = new DefaultTreeNode<>(new WorkPackage(), null);
        TreeNode<WorkPackage> projectNode = 
            new DefaultTreeNode<>(projectWorkPackage, root);
        buildWorkPackageTree(firstLevelWorkPackages, projectNode);
    }

    /**
     * Saves state of tree node when expanded.
     * 
     * @param node the expanded tree node
     */
    public void saveExpandedState(TreeNode<WorkPackage> node) {

        expandedStateMap.put(node.getRowKey(), node.isExpanded());

        for (TreeNode<WorkPackage> child : node.getChildren()) {
            saveExpandedState(child);
        }
    }

    /**
     * Restores expanded state of node if previously expanded. 
     * 
     * @param node previously expanded node
     */
    public void restoreExpandedState(TreeNode<WorkPackage> node) {
        Boolean expandedState = expandedStateMap.get(node.getRowKey());
        if (expandedState != null && expandedState) {
            node.setExpanded(true);
        }

        for (TreeNode<WorkPackage> child : node.getChildren()) {
            restoreExpandedState(child);
        }
    }

    /**
     * Builds work package tree with the given list of child work packages 
     * and parent work package node.
     * 
     * @param workPackages list of child work packages
     * @param parentNode parent work package node
     */
    private void buildWorkPackageTree(List<WorkPackage> workPackages,
        TreeNode<WorkPackage> parentNode) {

        System.out.println("building WP tree");
        for (WorkPackage wp : workPackages) {
            TreeNode<WorkPackage> wpNode = 
                new DefaultTreeNode<>(wp, parentNode);
            List<WorkPackage> wpChildren = 
                workPackageManager.getChildren(wp.getId());
            if (!wpChildren.isEmpty()) {
                buildWorkPackageTree(wpChildren, wpNode);
            }
        }
        System.out.println("finished building WP tree");
    }

    /**
     * Gets the root work package node.
     * 
     * @return the root work package node
     */
    public TreeNode<WorkPackage> getRoot() {
        return root;
    }

    /**
     * Sets the root work package node.
     *  
     * @param root the root work package node
     */
    public void setRoot(TreeNode<WorkPackage> root) {
        this.root = root;
    }

    /**
     * Gets the selected work package node.
     * 
     * @return the selected work package node
     */
    public TreeNode<WorkPackage> getSelectedNode() {
        return selectedNode;
    }

    /**
     * Sets the selected work package node.
     * 
     * @param selectedNode the selected work package node
     */
    public void setSelectedNode(TreeNode<WorkPackage> selectedNode) {
        this.selectedNode = selectedNode;
    }
}
