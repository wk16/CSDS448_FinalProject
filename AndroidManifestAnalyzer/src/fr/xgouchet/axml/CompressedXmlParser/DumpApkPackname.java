package fr.xgouchet.axml.CompressedXmlParser;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import static java.lang.Character.isDigit;
import static java.lang.Character.isUpperCase;
public class DumpApkPackname {
	public static void main(String[] args) throws IOException {
		//Total number each permission is used across all apks
		Hashtable<String, Integer> permsUsed = new Hashtable<String, Integer>();
		//Total number of permissions used per apk
		Hashtable<String, Integer> packagePerm = new Hashtable<String, Integer>();
		//Exact permissions used per apk
		Hashtable<String, String[]> permsUsedPackage = new Hashtable<String, String[]>();
		//Array for names of permissions used for a given apk
		ArrayList<String> permsByPack = new ArrayList<String>();

		String pkg;
		String folder = args[0];
		File[] files = new File(folder).listFiles();
		ArrayList<String> names = new ArrayList<>();
		for (File f : files) {
			String fileName = f.getAbsolutePath();
			pkg =f.getName();
			InputStream is = null;
			ZipFile zip = null;
			try {
				if (fileName.endsWith(".apk") || fileName.endsWith(".zip")) {

					String entryName = args.length > 1 ? args[1] : "AndroidManifest.xml";
					zip = new ZipFile(fileName);
					ZipEntry entry = zip.getEntry(entryName);
					is = zip.getInputStream(entry);
				} else {
					continue;
				}

				Document doc = new CompressedXmlParser().parseDOM(is);
				Node manifestnode = doc.getChildNodes().item(0);
				NamedNodeMap attrs = manifestnode.getAttributes();
				 dumpNode(doc.getChildNodes().item(0), "", pkg, permsUsed, packagePerm, permsUsedPackage, permsByPack);
				 System.out.println( "");
			} catch (Exception e) {
				System.err.println("Failed AXML decode: " + e);
				System.err.println(pkg);
				e.printStackTrace();
			}
			if (is != null) {
				is.close();
			}
			if (zip != null) {
				zip.close();
			}
		}
		PrintWriter out = new PrintWriter("appids.txt");
		for (String name : names) {
			out.println(name);
		}
		out.close();
		//System.out.println(permsUsed.toString());
		System.out.println(packagePerm.toString());
		System.out.println(permsUsedPackage.toString());
	}

	private static void dumpNode(Node node, String indent, String pkg, Hashtable permsUsed, Hashtable packagePerm, Hashtable permsUsedPackage, ArrayList permsByPack) {
		if (attrsToString(node.getAttributes()).contains("permission")) {
			Boolean run = true;
			String perm = attrsToString(node.getAttributes());
			String[] perms = perm.split("\\.");
			for (int i =0; i < perms.length; i++){
				run =true;
				for (int j =0; j<perms[i].length(); j++){
					if (perms[i].charAt(j)=='_' || isUpperCase(perms[i].charAt(j)) || isDigit(perms[i].charAt(j)) || perms[i].charAt(j)==']') {
						continue;
					}
					else{
						run = false;
					}
				}
				if (run){
					String temp=perms[i].substring(0,perms[i].length()-1);
					if (permsUsed.containsKey(temp)){
						int num = (int) permsUsed.get(temp);
						permsUsed.put(temp, num+1);
					}
					else{
						permsUsed.put(temp, 1);
					}
					if (packagePerm.containsKey(pkg)){
						int numInc = (int) packagePerm.get(pkg);
						packagePerm.put(pkg,numInc+1);
					}
					else{
						packagePerm.put(pkg, 1);
					}
					if (permsUsedPackage.containsKey(pkg)){
						ArrayList tempPkg = (ArrayList) permsUsedPackage.get(pkg);
						tempPkg.add(temp);
						permsUsedPackage.put(pkg,tempPkg);
					}
					else{
						permsByPack.clear();
						permsByPack.add(temp);
						permsUsedPackage.put(pkg,permsByPack);
					}

				}
			}
		}
		if (node.getNodeName().contains("uses-permission")) {
			if (!attrsToString(node.getAttributes()).contains("permission")) {
				Boolean run = true;
				String perm = attrsToString(node.getAttributes());

				String[] perms = perm.split("\\.");
				for (int i =0; i < perms.length; i++){
					run =true;
					for (int j =0; j<perms[i].length(); j++){
						if (perms[i].charAt(j)=='_' || isUpperCase(perms[i].charAt(j)) || isDigit(perms[i].charAt(j)) || perms[i].charAt(j)==']') {
							continue;
						}
						else{
							run = false;
						}
					}
					if (run) {
						String temp = perms[i].substring(0, perms[i].length() - 1);
						if (permsUsed.containsKey(temp)) {
							int num = (int) permsUsed.get(temp);
							permsUsed.put(temp, num + 1);
						} else {
							permsUsed.put(temp, 1);
						}
						if (packagePerm.containsKey(pkg)) {
							int numa = (int) packagePerm.get(pkg);
							packagePerm.put(pkg, numa + 1);
						} else {
							packagePerm.put(pkg, 1);
						}
						if (permsUsedPackage.containsKey(pkg)){
							ArrayList tempPkg = (ArrayList) permsUsedPackage.get(pkg);
							tempPkg.add(temp);
							permsUsedPackage.put(pkg,tempPkg);
						}
						else{
							permsByPack.clear();
							permsByPack.add(temp);
							permsUsedPackage.put(pkg,permsByPack);
						}
					}
				}
			}
		}

		NodeList children = node.getChildNodes();
		for (int i = 0, n = children.getLength(); i < n; ++i)
			dumpNode(children.item(i), indent + "   ",  pkg, permsUsed, packagePerm, permsUsedPackage, permsByPack);
	}

	private static String attrsToString(NamedNodeMap attrs) {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0, n = attrs.getLength(); i < n; ++i) {
			if (i != 0)
				sb.append(", ");
			Node attr = attrs.item(i);
			sb.append(attr.getNodeName() + "=" + attr.getNodeValue());
		}
		sb.append(']');
		return sb.toString();
	}

}