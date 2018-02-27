package gr.uom.java.bytecode.delegation;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;

import javax.swing.tree.DefaultMutableTreeNode;

import gr.uom.java.bytecode.ClassObject;
import gr.uom.java.bytecode.MethodInvocationObject;
import gr.uom.java.bytecode.MethodObject;
import gr.uom.java.bytecode.SignatureObject;
import gr.uom.java.bytecode.SystemObject;


public class DelegationTree {
    private DefaultMutableTreeNode rootNode;
    private SystemObject systemObject;

    public DelegationTree(SystemObject systemObject, MethodObject methodObject) {
        rootNode = new DefaultMutableTreeNode(methodObject.getSignature());
        this.systemObject = systemObject;
        getDelegations(rootNode);
    }

    private void getDelegations(DefaultMutableTreeNode node) {
        SignatureObject signature = (SignatureObject)node.getUserObject();
        ClassObject classObject = systemObject.getClassObject(signature.getClassName());
        MethodObject methodObject = classObject.getMethod(signature);
        if(methodObject != null) {
            ListIterator<MethodInvocationObject> it = methodObject.getMethodInvocationIterator();
            while(it.hasNext()) {
                MethodInvocationObject mio = it.next();
                int methodPos = systemObject.getPositionInClassList(methodObject.getClassName());
                int methodInvocationPos = systemObject.getPositionInClassList(mio.getOriginClassName());
                // methodPos != methodInvocationPos -> removes self-delegations
                // !existsNode(node.children(),mio.getSignature()) -> removes duplicate delegations
                // !existsNode(node.getUserObjectPath(),mio.getSignature()) -> avoids cyclic delegations
                if(methodInvocationPos != -1 && !existsNode(node.children(),mio.getSignature())
                        && !existsNode(node.getUserObjectPath(),mio.getSignature()) && methodPos != methodInvocationPos) {
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(mio.getSignature());
                    node.add(childNode);
                    getDelegations(childNode);
                }
            }
        }
    }

    private boolean existsNode(Object[] path, SignatureObject signature) {
        for(Object pathLevel : path) {
            if(signature.equals(pathLevel))
                return true;
        }
        return false;
    }

    private boolean existsNode(Enumeration children, SignatureObject signature) {
        while(children.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode)children.nextElement();
            SignatureObject childSignature = (SignatureObject)child.getUserObject();
            if(childSignature.equals(signature))
                return true;
        }
        return false;
    }

    public int getDepth() {
        return rootNode.getDepth();
    }

    public List<DelegationPath> getDelegationPathList() {
        List<DelegationPath> pathList = new ArrayList<DelegationPath>();

        DefaultMutableTreeNode leaf = rootNode.getFirstLeaf();
        while(leaf != null) {
            Object[] path = leaf.getUserObjectPath();
            DelegationPath delegationPath = new DelegationPath();
            for (Object pathLevel : path) {
                SignatureObject signature = (SignatureObject)pathLevel;
                delegationPath.addSignature(signature);
            }
            pathList.add(delegationPath);
            leaf = leaf.getNextLeaf();
        }
        return pathList;
    }
}
