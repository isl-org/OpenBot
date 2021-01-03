package org.openbot.common;

import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.openbot.R;
import org.openbot.model.Category;
import org.openbot.model.SubCategory;

public class Constants {

  public static final String USB_ACTION_DATA_RECEIVED = "usb.data_received";
  public static final String USB_ACTION_CONNECTION_ESTABLISHED = "usb.connection_established";
  public static final String USB_ACTION_CONNECTION_CLOSED = "usb.connection_closed";

  // Global
  public static final String ALL = "All";
  public static final String GLOBAL_VIEW = "Robot";
  public static final String CONTROLLER = "Controller";

  // Game
  public static final String GAME = "Game";
  public static final String FREE_ROAM = "Free Roam";
  public static final String AR_MODE = "AR Mode";

  // Data Collection
  public static final String DATA_COLLECTION = "Data Collection";
  public static final String LOCAL_SAVE_ON_PHONE = "Local (save On Phone)";
  public static final String EDGE_LOCAL_NETWORK = "Edge (local Network)";
  public static final String CLOUD_FIREBASE = "Cloud (firebase)";
  public static final String CROWD_SOURCE = "Crowd-source (post/accept Data Collection Tasks)";

  // AI
  public static final String AI = "AI";
  public static final String PERSON_FOLLOWING = "Person Following";
  public static final String OBJECT_NAV = "Object Nav (follow/search)";
  public static final String AUTONOMOUS_DRIVING = "Autonomous Driving";
  public static final String VISUAL_GOALS = "Visual Goals";
  public static final String SMART_VOICE = "Smart Voice (left/right/straight, Ar Core)";

  // Remote Access
  public static final String REMOTE_ACCESS = "Remote Access";
  public static final String WEB_INTERFACE = "Web Interface";
  public static final String ROS = "ROS";
  public static final String FLEET_MANAGEMENT = "Fleet Management";

  // Coding
  public static final String CODING = "Coding";
  public static final String BLOCK_BASED_PROGRAMMING = "Block-Based Programming";
  public static final String SCRIPTS = "Scripts";

  // Research
  public static final String RESEARCH = "Research";
  public static final String CLASSICAL_ROBOTICS_ALGORITHMS = "Classical Robotics Algorithms";
  public static final String BACKEND_FOR_LEARNING = "Backend For Learning";

  // Monitoring
  public static final String MONITORING = "Monitoring";
  public static final String SENSORS_FROM_CAR = "Sensors from Car";
  public static final String SENSORS_FROM_PHONE = "Sensors from Phone";
  public static final String MAP_VIEW = "Map View";

  @NotNull
  public static ArrayList<Category> getCategories() {
    ArrayList<Category> categories = new ArrayList<>();

    ArrayList<SubCategory> subCategories = new ArrayList<>();
    subCategories.add(new SubCategory(Constants.GLOBAL_VIEW, R.drawable.openbot_icon));
    categories.add(new Category(Constants.ALL, subCategories));

    /*
        subCategories = new ArrayList<>();
        subCategories.add(new SubCategory(Constants.CONTROLLER, R.drawable.ic_controller));
        subCategories.add(new SubCategory(Constants.FREE_ROAM, R.drawable.ic_game));
        subCategories.add(new SubCategory(Constants.AR_MODE, R.drawable.ic_game));
        categories.add(new Category(Constants.GAME, subCategories));

        subCategories = new ArrayList<>();
        subCategories.add(new SubCategory(Constants.LOCAL_SAVE_ON_PHONE, R.drawable.ic_storage));
        subCategories.add(new SubCategory(Constants.EDGE_LOCAL_NETWORK, R.drawable.ic_network));
        subCategories.add(new SubCategory(Constants.CLOUD_FIREBASE, R.drawable.ic_cloud_upload));
        subCategories.add(new SubCategory(Constants.CROWD_SOURCE, R.drawable.openbot_icon));
        categories.add(new Category(Constants.DATA_COLLECTION, subCategories));

        subCategories = new ArrayList<>();
        subCategories.add(new SubCategory(Constants.PERSON_FOLLOWING, R.drawable.ic_person_search));
        subCategories.add(new SubCategory(Constants.OBJECT_NAV, R.drawable.openbot_icon));
        subCategories.add(new SubCategory(Constants.SMART_VOICE, R.drawable.ic_voice_over));
        subCategories.add(new SubCategory(Constants.AUTONOMOUS_DRIVING, R.drawable.openbot_icon));
        subCategories.add(new SubCategory(Constants.VISUAL_GOALS, R.drawable.openbot_icon));
        categories.add(new Category(Constants.AI, subCategories));

        subCategories = new ArrayList<>();
        subCategories.add(new SubCategory(Constants.WEB_INTERFACE, R.drawable.openbot_icon));
        subCategories.add(new SubCategory(Constants.ROS, R.drawable.openbot_icon));
        subCategories.add(new SubCategory(Constants.FLEET_MANAGEMENT, R.drawable.openbot_icon));
        categories.add(new Category(Constants.REMOTE_ACCESS, subCategories));

        subCategories = new ArrayList<>();
        subCategories.add(new SubCategory(Constants.BLOCK_BASED_PROGRAMMING, R.drawable.ic_code));
        subCategories.add(new SubCategory(Constants.SCRIPTS, R.drawable.ic_code));
        categories.add(new Category(Constants.CODING, subCategories));

        subCategories = new ArrayList<>();
        subCategories.add(
            new SubCategory(Constants.CLASSICAL_ROBOTICS_ALGORITHMS, R.drawable.openbot_icon));
        subCategories.add(new SubCategory(Constants.BACKEND_FOR_LEARNING, R.drawable.openbot_icon));
        categories.add(new Category(Constants.RESEARCH, subCategories));

        subCategories = new ArrayList<>();
        subCategories.add(new SubCategory(Constants.SENSORS_FROM_CAR, R.drawable.ic_electric_car));
        subCategories.add(new SubCategory(Constants.SENSORS_FROM_PHONE, R.drawable.ic_phonelink));
        subCategories.add(new SubCategory(Constants.MAP_VIEW, R.drawable.ic_map));
        categories.add(new Category(Constants.MONITORING, subCategories));
    */

    return categories;
  }
}
