package fr.xgouchet.axml.CompressedXmlParser;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.lang.Character.isDigit;
import static java.lang.Character.isUpperCase;

/*
Authors: Will Kunz and Meira Farhi
Course: CSDS 448 Smartphone Security
Spring 2020
Final Project
 */

public class DumpApkPackname {
	public static void main(String[] args) throws IOException {
		//Total number each permission is used across all apks
		Hashtable<String, Integer> permsUsed = new Hashtable<String, Integer>();
		//Total number of permissions used per apk
		Hashtable<String, Integer> packagePerm = new Hashtable<String, Integer>();
		//Exact permissions used per apk
		Map<String, List<String>> permsUsedPackage = new HashMap<>();
		//Array for names of permissions used for a given apk
		List <String> permsByPack = new ArrayList<>();


		String pkg;
		String folder = args[0];
		File[] files = new File("/Users/will/Desktop/Smartphone Project Apks/Ransomware_Test").listFiles();
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

		Iterator hmIterator = permsUsedPackage.entrySet().iterator();
		ArrayList<String> value;
		//String containing all permissions possible in android comma seperated.
		String allPermissions = "ACCESS_ALL_DOWNLOADS,ACCESS_BLUETOOTH_SHARE,ACCESS_CACHE_FILESYSTEM,ACCESS_CHECKIN_PROPERTIES,ACCESS_CONTENT_PROVIDERS_EXTERNALLY,ACCESS_DOWNLOAD_MANAGER,ACCESS_DOWNLOAD_MANAGER_ADVANCED,ACCESS_DRM_CERTIFICATES,ACCESS_EPHEMERAL_APPS,ACCESS_FM_RADIO,ACCESS_INPUT_FLINGER,ACCESS_KEYGUARD_SECURE_STORAGE,ACCESS_LOCATION_EXTRA_COMMANDS,ACCESS_MOCK_LOCATION,ACCESS_MTP,ACCESS_NETWORK_CONDITIONS,ACCESS_NETWORK_STATE,ACCESS_NOTIFICATIONS,ACCESS_NOTIFICATION_POLICY,ACCESS_PDB_STATE,ACCESS_SURFACE_FLINGER,ACCESS_VOICE_INTERACTION_SERVICE,ACCESS_VR_MANAGER,ACCESS_WIFI_STATE,ACCESS_WIMAX_STATE,ACCOUNT_MANAGER,ALLOW_ANY_CODEC_FOR_PLAYBACK,ASEC_ACCESS,ASEC_CREATE,ASEC_DESTROY,ASEC_MOUNT_UNMOUNT,ASEC_RENAME,AUTHENTICATE_ACCOUNTS,BACKUP,BATTERY_STATS,BIND_ACCESSIBILITY_SERVICE,BIND_APPWIDGET,BIND_CARRIER_MESSAGING_SERVICE,BIND_CARRIER_SERVICES,BIND_CHOOSER_TARGET_SERVICE,BIND_CONDITION_PROVIDER_SERVICE,BIND_CONNECTION_SERVICE,BIND_DEVICE_ADMIN,BIND_DIRECTORY_SEARCH,BIND_DREAM_SERVICE,BIND_INCALL_SERVICE,BIND_INPUT_METHOD,BIND_INTENT_FILTER_VERIFIER,BIND_JOB_SERVICE,BIND_KEYGUARD_APPWIDGET,BIND_MIDI_DEVICE_SERVICE,BIND_NFC_SERVICE,BIND_NOTIFICATION_LISTENER_SERVICE,BIND_NOTIFICATION_RANKER_SERVICE,BIND_PACKAGE_VERIFIER,BIND_PRINT_RECOMMENDATION_SERVICE,BIND_PRINT_SERVICE,BIND_PRINT_SPOOLER_SERVICE,BIND_QUICK_SETTINGS_TILE,BIND_REMOTEVIEWS,BIND_REMOTE_DISPLAY,BIND_ROUTE_PROVIDER,BIND_RUNTIME_PERMISSION_PRESENTER_SERVICE,BIND_SCREENING_SERVICE,BIND_TELECOM_CONNECTION_SERVICE,BIND_TEXT_SERVICE,BIND_TRUST_AGENT,BIND_TV_INPUT,BIND_TV_REMOTE_SERVICE,BIND_VOICE_INTERACTION,BIND_VPN_SERVICE,BIND_VR_LISTENER_SERVICE,BIND_WALLPAPER,BLUETOOTH,BLUETOOTH_ADMIN,BLUETOOTH_MAP,BLUETOOTH_PRIVILEGED,BLUETOOTH_STACK,BRICK,BROADCAST_CALLLOG_INFO,BROADCAST_NETWORK_PRIVILEGED,BROADCAST_PACKAGE_REMOVED,BROADCAST_PHONE_ACCOUNT_REGISTRATION,BROADCAST_SMS,BROADCAST_STICKY,BROADCAST_WAP_PUSH,CACHE_CONTENT,CALL_PRIVILEGED,CAMERA_DISABLE_TRANSMIT_LED,CAMERA_SEND_SYSTEM_EVENTS,CAPTURE_AUDIO_HOTWORD,CAPTURE_AUDIO_OUTPUT,CAPTURE_SECURE_VIDEO_OUTPUT,CAPTURE_TV_INPUT,CAPTURE_VIDEO_OUTPUT,CARRIER_FILTER_SMS,CHANGE_APP_IDLE_STATE,CHANGE_BACKGROUND_DATA_SETTING,CHANGE_COMPONENT_ENABLED_STATE,CHANGE_CONFIGURATION,CHANGE_DEVICE_IDLE_TEMP_WHITELIST,CHANGE_NETWORK_STATE,CHANGE_WIFI_MULTICAST_STATE,CHANGE_WIFI_STATE,CHANGE_WIMAX_STATE,CLEAR_APP_CACHE,CLEAR_APP_GRANTED_URI_PERMISSIONS,CLEAR_APP_USER_DATA,CONFIGURE_DISPLAY_COLOR_TRANSFORM,CONFIGURE_WIFI_DISPLAY,CONFIRM_FULL_BACKUP,CONNECTIVITY_INTERNAL,CONTROL_INCALL_EXPERIENCE,CONTROL_KEYGUARD,CONTROL_LOCATION_UPDATES,CONTROL_VPN,CONTROL_WIFI_DISPLAY,COPY_PROTECTED_DATA,CREATE_USERS,CRYPT_KEEPER,DELETE_CACHE_FILES,DELETE_PACKAGES,DEVICE_POWER,DIAGNOSTIC,DISABLE_KEYGUARD,DISPATCH_NFC_MESSAGE,DISPATCH_PROVISIONING_MESSAGE,DOWNLOAD_CACHE_NON_PURGEABLE,DUMP,DVB_DEVICE,EXPAND_STATUS_BAR,FACTORY_TEST,FILTER_EVENTS,FLASHLIGHT,FORCE_BACK,FORCE_STOP_PACKAGES,FRAME_STATS,FREEZE_SCREEN,GET_ACCOUNTS_PRIVILEGED,GET_APP_GRANTED_URI_PERMISSIONS,GET_APP_OPS_STATS,GET_DETAILED_TASKS,GET_INTENT_SENDER_INTENT,GET_PACKAGE_IMPORTANCE,GET_PACKAGE_SIZE,GET_PASSWORD,GET_PROCESS_STATE_AND_OOM_SCORE,GET_TASKS,GET_TOP_ACTIVITY_INFO,GLOBAL_SEARCH,GLOBAL_SEARCH_CONTROL,GRANT_RUNTIME_PERMISSIONS,HARDWARE_TEST,HDMI_CEC,INJECT_EVENTS,INSTALL_GRANT_RUNTIME_PERMISSIONS,INSTALL_LOCATION_PROVIDER,INSTALL_PACKAGES,INTENT_FILTER_VERIFICATION_AGENT,INTERACT_ACROSS_USERS,INTERACT_ACROSS_USERS_FULL,INTERNAL_SYSTEM_WINDOW,INTERNET,INVOKE_CARRIER_SETUP,KILL_BACKGROUND_PROCESSES,KILL_UID,LAUNCH_TRUST_AGENT_SETTINGS,LOCAL_MAC_ADDRESS,LOCATION_HARDWARE,LOOP_RADIO,MANAGE_ACCOUNTS,MANAGE_ACTIVITY_STACKS,MANAGE_APP_OPS_RESTRICTIONS,MANAGE_APP_TOKENS,MANAGE_CA_CERTIFICATES,MANAGE_DEVICE_ADMINS,MANAGE_DOCUMENTS,MANAGE_FINGERPRINT,MANAGE_MEDIA_PROJECTION,MANAGE_NETWORK_POLICY,MANAGE_NOTIFICATIONS,MANAGE_PROFILE_AND_DEVICE_OWNERS,MANAGE_SOUND_TRIGGER,MANAGE_USB,MANAGE_USERS,MANAGE_VOICE_KEYPHRASES,MASTER_CLEAR,MEDIA_CONTENT_CONTROL,MODIFY_APPWIDGET_BIND_PERMISSIONS,MODIFY_AUDIO_ROUTING,MODIFY_AUDIO_SETTINGS,MODIFY_CELL_BROADCASTS,MODIFY_DAY_NIGHT_MODE,MODIFY_NETWORK_ACCOUNTING,MODIFY_PARENTAL_CONTROLS,MODIFY_PHONE_STATE,MOUNT_FORMAT_FILESYSTEMS,MOUNT_UNMOUNT_FILESYSTEMS,MOVE_PACKAGE,NET_ADMIN,NET_TUNNELING,NFC,NFC_HANDOVER_STATUS,NOTIFY_PENDING_SYSTEM_UPDATE,OBSERVE_GRANT_REVOKE_PERMISSIONS,OEM_UNLOCK_STATE,OVERRIDE_WIFI_CONFIG,PACKAGE_USAGE_STATS,PACKAGE_VERIFICATION_AGENT,PACKET_KEEPALIVE_OFFLOAD,PEERS_MAC_ADDRESS,PERFORM_CDMA_PROVISIONING,PERFORM_SIM_ACTIVATION,PERSISTENT_ACTIVITY,PROCESS_CALLLOG_INFO,PROCESS_PHONE_ACCOUNT_REGISTRATION,PROVIDE_TRUST_AGENT,QUERY_DO_NOT_ASK_CREDENTIALS_ON_BOOT,READ_BLOCKED_NUMBERS,READ_DREAM_STATE,READ_FRAME_BUFFER,READ_INPUT_STATE,READ_INSTALL_SESSIONS,READ_LOGS,READ_NETWORK_USAGE_HISTORY,READ_OEM_UNLOCK_STATE,READ_PRECISE_PHONE_STATE,READ_PRIVILEGED_PHONE_STATE,READ_PROFILE,READ_SEARCH_INDEXABLES,READ_SOCIAL_STREAM,READ_SYNC_SETTINGS,READ_SYNC_STATS,READ_USER_DICTIONARY,READ_WIFI_CREDENTIAL,REAL_GET_TASKS,REBOOT,RECEIVE_BLUETOOTH_MAP,RECEIVE_BOOT_COMPLETED,RECEIVE_DATA_ACTIVITY_CHANGE,RECEIVE_EMERGENCY_BROADCAST,RECEIVE_MEDIA_RESOURCE_USAGE,RECEIVE_STK_COMMANDS,RECEIVE_WIFI_CREDENTIAL_CHANGE,RECOVERY,REGISTER_CALL_PROVIDER,REGISTER_CONNECTION_MANAGER,REGISTER_SIM_SUBSCRIPTION,REGISTER_WINDOW_MANAGER_LISTENERS,REMOTE_AUDIO_PLAYBACK,REMOVE_DRM_CERTIFICATES,REMOVE_TASKS,REORDER_TASKS,REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,REQUEST_INSTALL_PACKAGES,RESET_FINGERPRINT_LOCKOUT,RESET_SHORTCUT_MANAGER_THROTTLING,RESTART_PACKAGES,RETRIEVE_WINDOW_CONTENT,RETRIEVE_WINDOW_TOKEN,REVOKE_RUNTIME_PERMISSIONS,SCORE_NETWORKS,SEND_CALL_LOG_CHANGE,SEND_DOWNLOAD_COMPLETED_INTENTS,SEND_RESPOND_VIA_MESSAGE,SEND_SMS_NO_CONFIRMATION,SERIAL_PORT,SET_ACTIVITY_WATCHER,SET_ALWAYS_FINISH,SET_ANIMATION_SCALE,SET_DEBUG_APP,SET_INPUT_CALIBRATION,SET_KEYBOARD_LAYOUT,SET_ORIENTATION,SET_POINTER_SPEED,SET_PREFERRED_APPLICATIONS,SET_PROCESS_LIMIT,SET_SCREEN_COMPATIBILITY,SET_TIME,SET_TIME_ZONE,SET_WALLPAPER,SET_WALLPAPER_COMPONENT,SET_WALLPAPER_HINTS,SHUTDOWN,SIGNAL_PERSISTENT_PROCESSES,START_ANY_ACTIVITY,START_PRINT_SERVICE_CONFIG_ACTIVITY,START_TASKS_FROM_RECENTS,STATUS_BAR,STATUS_BAR_SERVICE,STOP_APP_SWITCHES,STORAGE_INTERNAL,SUBSCRIBED_FEEDS_READ,SUBSCRIBED_FEEDS_WRITE,SUBSTITUTE_NOTIFICATION_APP_NAME,SYSTEM_ALERT_WINDOW,TABLET_MODE,TEMPORARY_ENABLE_ACCESSIBILITY,TETHER_PRIVILEGED,TRANSMIT_IR,TRUST_LISTENER,TV_INPUT_HARDWARE,TV_VIRTUAL_REMOTE_CONTROLLER,UPDATE_APP_OPS_STATS,UPDATE_CONFIG,UPDATE_DEVICE_STATS,UPDATE_LOCK,UPDATE_LOCK_TASK_PACKAGES,USER_ACTIVITY,USE_CREDENTIALS,VIBRATE,WAKE_LOCK,WRITE_APN_SETTINGS,WRITE_BLOCKED_NUMBERS,WRITE_DREAM_STATE,WRITE_GSERVICES,WRITE_MEDIA_STORAGE,WRITE_PROFILE,WRITE_SECURE_SETTINGS,WRITE_SETTINGS,WRITE_SMS,WRITE_SOCIAL_STREAM,WRITE_SYNC_SETTINGS,WRITE_USER_DICTIONARY";
		//Array containing all permissions possible in android.
		List<String> allPermissionsList= Arrays.asList(allPermissions.split(","));
		//Array for if a given apk has each permission. 1 for True, 0 for False. The index for each permission is the same as allPermissionsList
		List<List<Integer>> binaryPermissionList = new ArrayList<>();
		//Loops through all apks in the permsUsedPackage map and creates the binaryPermissionList for each apk.
		while (hmIterator.hasNext()) {
			Map.Entry mapElement = (Map.Entry)hmIterator.next();
			value = (ArrayList<String>) mapElement.getValue();
			List<Integer> hasPermission = new ArrayList<>();
			for (int i =0; i<allPermissionsList.size(); i++){
				hasPermission.add(0);
				if (value.contains(allPermissionsList.get(i))) {
					hasPermission.set(i,1);
				}
			}
			binaryPermissionList.add(hasPermission);
		}
		//Writes binaryPermissionList to a CSV file.
		try (Writer writer = new FileWriter("ransomwareTest.csv")){
			for (List<Integer> entry : binaryPermissionList){
				writer.append(entry.toString()+"\n");
			}
		} catch (IOException ex){
			ex.printStackTrace(System.err);
		}
	}
	//Extracts permissions from each apk.
	private static void dumpNode(Node node, String indent, String pkg, Hashtable permsUsed, Hashtable packagePerm, Map<String, List<String>> permsUsedPackage, List permsByPack) {
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
					if (permsUsedPackage.containsKey(pkg)){
						List <String> tempPkg = new ArrayList<>();
						for (String permission : permsUsedPackage.get(pkg)) {
							tempPkg.add(permission);
						}
						tempPkg.add(temp);
						permsUsedPackage.remove(pkg);
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
							List <String> tempPkg = new ArrayList<>();
							for (String permission : permsUsedPackage.get(pkg)) {
								tempPkg.add(permission);
							}
							tempPkg.add(temp);
							permsUsedPackage.remove(pkg);
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