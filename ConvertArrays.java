/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package convertarrays;
import java.util.*;

//{
//	‘one’:
//	{
//		‘two’ : 3,
//		‘four’:  [5,6,7]
//	}
//	‘eight’:
//	{
//		‘nine’:
//		{
//			‘ten’ :11
//		}
//	}
//}

//{	'one/two':3,
//	'one/four/0':5,
//	'one/four/1':6,
//	'one/four/2':7,
//	'eight/nine/ten':11
//}

/**
 *
 * @author oornahaber
 */
public class ConvertArrays {

  final static String MultiArray1 = "{\n 'one':\n {\n 'two':3,\n 'four': [5,6,7]\n },\n 'eight':\n {\n 'nine':\n {\n 'ten':11\n }\n }\n }\n";
  final static String MultiArray2 =
          "{\n" +
"	'one':\n" +
"	{\n" +
"		'two':3,\n" +
"		'four':[5,6,7],\n" +
"		'eleven':12\n" +
"	},\n" +
"	'eight':\n" +
"	{\n" +
"		'nine':\n" +
"		{\n" +
"			'ten':11\n" +
"		}\n" +
"	},\n" +
"	'thirteen':\n" +
"	{\n" +
"		'fourteen':\n" +
"		{\n" +
"			'sixteen':15,\n" +
"			'seventeen':18\n" +
"		}\n" +
"	}\n" +
"}";
  final static String OneDimensionalArray1 = "{\n" +
            "	'one/two':3,\n" +
            "	'one/four/0':5,\n" +
            "	'one/four/1':6,\n" +
            "	'one/four/2':7,\n" +
            "	'eight/nine/ten':11\n" +
            "}\n";
  final static String OneDimensionalArray2 = "{\n" +
            "	'one/two':3,\n" +
            "	'one/four/0':5,\n" +
            "	'one/four/1':6,\n" +
            "	'one/four/2':7,\n" +
            "	'one/eleven':12,\n" +
            "	'eight/nine/ten':11,\n" +
            "   'thirteen/fourteen/sixteen:15,\n" +
            "   'thirteen/fourteen/seventeen:18\n" +
            "}\n";
  /**
     * @param args the command line arguments
     */
     // Output by level
    public static String writeOutput (Scanner in, int level, Map<Integer, String> elements, String [] keyValuePair) {
        StringBuilder outputBuilder = new StringBuilder ();    
        StringBuilder strBuilder = new StringBuilder ("\t'");
        for (int iLevel = 1; iLevel < level; iLevel++) {
            String element = elements.get (iLevel);
            if (element != null) {
                strBuilder.append (element + "/");
                
            }
            // else report a problem
        }
        strBuilder.append (keyValuePair[0]);
        if (keyValuePair[1].indexOf ("[") == -1) {
           strBuilder.append ("':" + keyValuePair[1]);
           if (in.hasNext () && (strBuilder.lastIndexOf ("," ) != strBuilder.length () -1)) {
               strBuilder.append (",");
           }
           strBuilder.append ("\n");
           outputBuilder.append (strBuilder.toString ());
        }
        else {
            String value = keyValuePair[1].substring (1);
            String [] arrayElements = value.split (",|\\]");  // \\[|
            for (int i=0; i< arrayElements.length; i++) {
                StringBuilder elementstrBuilder = new StringBuilder (strBuilder);
                String iElement = new String ("/"+ String.valueOf (i) + "':" + arrayElements[i]);
                elementstrBuilder.append (iElement);
                if (i != arrayElements.length - 1 || in.hasNext ()) {
                     elementstrBuilder.append (",\n");
                }
                outputBuilder.append (elementstrBuilder.toString ());
            }
        }
        return outputBuilder.toString ();
    }
    
      
    public static boolean convertToOneDimensional (String multiDimensionalArray) {
        int level = 1;
        Map<Integer, String> elements = new HashMap<Integer, String> ();
        Scanner in = new Scanner (multiDimensionalArray);
        StringBuilder strBuilder = new StringBuilder ("{\n");
        while (in.hasNext()) {
          try {
            String line = in.nextLine ();            
            if (line != null) {

                if (line.endsWith ("{")) {
                    continue;
                }
                line = line.replaceAll ("'", "");
                line = line.replaceAll("\\s+","");
                int separatorIndex = line.lastIndexOf (":");
                if (separatorIndex == line.length () - 1)   {// separator at end of line
                   
                    String elementValue = line.substring (0,separatorIndex);
                    elements.put (level++, elementValue);
                }
                else if (separatorIndex != -1) { // key value pair
                    String [] keyValuePair = line.split (":", -1);
                    if (keyValuePair.length != 2) {
                        return false;   // invalid read format
                    }
                    strBuilder.append (writeOutput (in, level, elements, keyValuePair));
                }
                else if (separatorIndex == -1 && line.indexOf ("}") != -1) { // end of block
                    level--;                            
                }
                   
            } // if input line != null
         }
         catch (NoSuchElementException ex) {
            System.out.println("Error reading input array.");
            return false;
         }                                    
       }
       int lastIndex = strBuilder.length() -2;
       if (strBuilder.charAt (lastIndex) == ',')  {
           strBuilder = strBuilder.deleteCharAt(lastIndex);
       }
       strBuilder.append ("}");
       System.out.println (strBuilder);
       in.close ();
       return true;
    }
    

    /* A Node may be a child node or a leaf node.
    * A child node is like a branch node. It may have one or more branch nodes or
    * A child node may have one or more leaf nodes. 
    *
    */
    class Node {
        String value;
        Node parent;
        boolean isALeafNode;
        boolean isLeafNode () {
           return isALeafNode; 
        }
        List<Node> childNodes;
        Node (String value, boolean isALeafNode) {
            this.isALeafNode = isALeafNode;
            this.value = value;
            childNodes = new ArrayList<Node> ();
        }
        void setParent (Node parentNode) {
            this.parent = parentNode;
        }
        Node addChildNode (String childNodeValue) {
            if (childNodes.indexOf (childNodeValue) != -1) {
                System.out.printf ("Already has a child named: %s.\n", childNodeValue);
                return null;
            }
            else {
                Node childNode = new Node (childNodeValue, false);
                childNodes.add (childNode);
                return (childNodes.get (childNodes.size () -1));
            }
        }
        Node addLeafNode (String leafNodeValue) {
            Node leafNode =  new Node (leafNodeValue, true); 
            //assert (childNodes.size () == 0);
            childNodes.add (leafNode); 
            return leafNode;
        }
        void addLeafNode (Node leafNode) {
            childNodes.add (leafNode);
        }
        
        void SetLeafNode (String appendValue) {
            isALeafNode = true;
            value = new String ("'" + value + "':" + appendValue);
            
        }
        
        Node getLeafNode () {
           if (childNodes.size () == 1 && childNodes.get (0).isLeafNode ())  {
               return childNodes.get(0);
           }
           return null;
        }
        Node getChildNode (String childNodeValue) {
            for (Node iNode : childNodes) {
                if (iNode.value.equals (childNodeValue)) {
                    return iNode;
                }
                else if (iNode.isLeafNode () && iNode.value.startsWith(childNodeValue)) {
                    return iNode;
                }
            }
            return null;
        }
        Node getLastChildNode () {
            int nChildNodes = childNodes.size ();
            if (nChildNodes > 1) {
                return childNodes.get (nChildNodes -1);
            }
            return null;
        }
        /* Return the leaf nodes of last child node  */
        Node getLastBranchLeafNode () {
            int nChildNodes = childNodes.size ();
            if (nChildNodes == 0) {
                System.out.println ("Either there is no last branch or Invalid array structure. ");
                return null;
            }
            Node lastNode = childNodes.get (nChildNodes - 1);
            if (lastNode.isLeafNode ()) {
                return lastNode;
            }
            else {
                return lastNode.getLastBranchLeafNode ();
            }
        }
        
        public String toString () {
            if (isLeafNode ()) {
                String[] keyValuePair = value.split (":", -1);
                String displayValue = new String ("'" + keyValuePair[0] + "':" + keyValuePair[1]);
                return displayValue;
            }
            else {
                if (value.equals ("{"))
                    return "";
                else
                    return "'" + value + "':";
            }
        }
        String printNodes (String padStr) {
            StringBuilder outString = new StringBuilder (padStr);
            outString.append (this + "\n");
            if (!isLeafNode ())
                outString.append (padStr + "{\n");
            if (childNodes.size () > 0) {
                Node lastNode = childNodes.get (childNodes.size () -1);
                if (lastNode.isLeafNode ()) {
                    int lastCommaIndex = lastNode.value.lastIndexOf (",");
                    if (lastCommaIndex != -1) {
                        lastNode.value = lastNode.value.substring (0, lastNode.value.length()-1);
                    }
                }
            }
            for (Node iChild : childNodes) {
               outString.append (iChild.printNodes (padStr + "\t"));
            }
            if (!isLeafNode ()) {
                outString.append (padStr + "}");
                boolean addSeparator = false; 
                if (parent != null && parent.childNodes.size () > 1) {
                    Node parentLastNode = parent.childNodes.get (parent.childNodes.size () -1);
                    addSeparator = !(parentLastNode.value.equals (this.value));
                }
                if (addSeparator) {
                    outString.append (",");
                }
                outString.append ("\n");
            }
            return outString.toString ();
        }
        
    }
    
    public boolean convertToMultiDimensionalArray (String oneDimensionalArray) {
        Node root = new Node ("{", false);
        boolean arrayLeafProcessing = false;
        Scanner in = new Scanner (oneDimensionalArray);
        while (in.hasNext()) {
            try {                
                String line = in.nextLine ();            
                if (line != null) {

                    line = line.replaceAll ("'", "");
                    line = line.replaceAll("\\s+","");
                    if (line.endsWith ("{")) {
                        continue;
                    }
                    else if (line.endsWith ("}")) {
                        break;
                        // Proceed with output
                    }
                    String [] levels = line.split ("/", -1);
                    if (levels.length == 0) {
                        System.out.println ("Invalid Input: Expecting multi dimensional array");
                        return false;
                    }
                    int numOfLevels = levels.length;
                    int leafArrayIndex = -1;
                    String [] leafNodeElements = levels[numOfLevels -1].split (":", -1);
                    if (leafNodeElements.length != 2) {
                        System.out.println ("Inavlid input: Expecting a key value pair for a leaf node.");
                        return false;
                    }
                    try
                    {
                            leafArrayIndex = Integer.parseInt (leafNodeElements[0]);
                    }
                    catch (NumberFormatException ex) {
                            // Ignore. Testing if the last node is an integer, index to an array node
                    }
                    if (arrayLeafProcessing && leafArrayIndex == -1) {
                        // Complete key value assignment of an array leaf
                        arrayLeafProcessing = false;
                        Node leafNode = root.getLastBranchLeafNode ();
                        if (leafNode == null) {
                            System.out.println ("Invalid procesing of an array leaf node.");
                            return false;
                        }
                        else {
                            int lastCommaIndex = leafNode.value.lastIndexOf (",");
                            if (lastCommaIndex != -1) {
                                leafNode.value = leafNode.value.substring (0, lastCommaIndex);
                                leafNode.value += "],";
                            }
                        }
                    }
                    // Iterate branches:
                    Node curNode = root;
                    for (int iLevel =0; iLevel < numOfLevels-1; iLevel++) {
                        String levelName = levels[iLevel];
                        Node childNode = curNode.getChildNode (levelName);
                        if (childNode == null) {
                            childNode = curNode.addChildNode (levelName);
                            childNode.setParent (curNode);
                        }
                        curNode = childNode;
                    }
                    // Add the leaf node to curNode:
                    if (leafArrayIndex != -1)  {  // It's an array leaf
                        arrayLeafProcessing = true;
                        if (leafArrayIndex == 0) {
                            Node parentNode = curNode.parent;
                            Node leafNode = new Node (curNode.value + ":[" + leafNodeElements[1], true);
                            leafNode.setParent (parentNode);
                            parentNode.childNodes.remove (curNode);
                            parentNode.addLeafNode (leafNode);
                        }
                        else {
                            curNode.value +=  leafNodeElements[1];
                        }
                    }
                    else {  // regular leaf node
                        curNode.addLeafNode (levels[numOfLevels -1]);
                    }
                }
            }
            catch (NoSuchElementException ex) {
                System.out.println("Error reading input array.");
                return false;
            }                                    
        }   // end while input
        // Print the multi dimensional array:
        String padding = new String ();
        System.out.println (root.printNodes (padding));
            
        
        return true;
    }
     public static void main(String[] args) {
        // Test application logic
        convertToOneDimensional (MultiArray1);
        convertToOneDimensional (MultiArray2);
        
        ConvertArrays arrayConversion = new ConvertArrays ();
        arrayConversion.convertToMultiDimensionalArray (OneDimensionalArray1);
        arrayConversion.convertToMultiDimensionalArray (OneDimensionalArray2);
    }
    
}
