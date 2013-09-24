package jmri.jmrit.operations.trains;

/**
 * A convenient place to access operations xml element and attribute names.
 * 
 * @author Daniel Boudreau Copyright (C) 2013
 * @version $Revision: 22156 $
 * 
 */
public class Xml {

	// Common to operation xml files
	static final String ID = "id"; // NOI18N
	static final String NAME = "name"; // NOI18N
	static final String COMMENT = "comment"; // NOI18N
	
	static final String TRUE = "true"; // NOI18N
	static final String FALSE = "false"; // NOI18N
	
	// train.java
	static final String TRAIN = "train"; // NOI18N
	static final String DESCRIPTION = "description"; // NOI18N
	static final String DEPART_HOUR = "departHour"; // NOI18N
	static final String DEPART_MINUTE = "departMinute"; // NOI18N
	static final String ROUTE = "route"; // NOI18N
	static final String SKIPS = "skips"; // NOI18N
	static final String LOCATION = "location"; // NOI18N
	static final String ROUTE_ID = "routeId"; // NOI18N old format
	static final String SKIP = "skip"; // NOI18N
	static final String CAR_TYPES = "carTypes"; // NOI18N
	static final String TYPES = "types"; // NOI18N
	static final String CAR_TYPE = "carType"; // NOI18N
	static final String LOCO_TYPE = "locoType"; // NOI18N
	static final String CAR_ROAD_OPERATION = "carRoadOperation"; // NOI18N	should have been option not operation
	static final String CAR_ROAD_OPTION = "carRoadOption"; // NOI18N
	static final String CAR_ROADS = "carRoads"; // NOI18N
	static final String CAR_ROAD = "carRoad"; // NOI18N
	static final String CAR_LOAD_OPTION = "carLoadOption"; // NOI18N
	static final String CAR_LOADS = "carLoads"; // NOI18N
	static final String CAR_LOAD = "carLoad"; // NOI18N
	static final String CAR_OWNER_OPTION = "carOwnerOption"; // NOI18N
	static final String BUILT_START_YEAR = "builtStartYear"; // NOI18N
	static final String BUILT_END_YEAR = "builtEndYear"; // NOI18N
	static final String CAR_OWNERS = "carOwners"; // NOI18N
	static final String CAR_OWNER = "carOwner"; // NOI18N
	
	static final String NUMBER_ENGINES = "numberEngines"; // NOI18N
	static final String LEG2_ENGINES = "leg2Engines"; // NOI18N
	static final String LEG3_ENGINES = "leg3Engines"; // NOI18N
	
	static final String ENGINE_ROAD = "engineRoad"; // NOI18N
	static final String LEG2_ROAD = "leg2Road"; // NOI18N
	static final String LEG3_ROAD = "leg3Road"; // NOI18N
	
	static final String ENGINE_MODEL = "engineModel"; // NOI18N
	static final String LEG2_MODEL = "leg2Model"; // NOI18N
	static final String LEG3_MODEL = "leg3Model"; // NOI18N
	
	static final String REQUIRES = "requires"; // NOI18N
	static final String CABOOSE_ROAD = "cabooseRoad"; // NOI18N
	static final String LEG2_CABOOSE_ROAD = "leg2CabooseRoad"; // NOI18N
	static final String LEG3_CABOOSE_ROAD = "leg3CabooseRoad"; // NOI18N
	
	static final String LEG2_OPTIONS = "leg2Options"; // NOI18N
	static final String LEG3_OPTIONS = "leg3Options"; // NOI18N
	
	static final String BUILD_NORMAL = "buildNormal"; // NOI18N
	static final String TO_TERMINAL = "toTerminal"; // NOI18N
	static final String ALLOW_LOCAL_MOVES = "allowLocalMoves"; // NOI18N
	static final String ALLOW_THROUGH_CARS = "allowThroughCars"; // NOI18N
	static final String ALLOW_RETURN = "allowReturn"; // NOI18N
	
	static final String BUILT = "built"; // NOI18N
	static final String BUILD = "build"; // NOI18N
	static final String BUILD_FAILED = "buildFailed"; // NOI18N
	static final String BUILD_FAILED_MESSAGE = "buildFailedMessage"; // NOI18N
	static final String PRINTED = "printed"; // NOI18N
	static final String MODIFIED = "modified"; // NOI18N
	static final String SWITCH_LIST_STATUS = "switchListStatus"; // NOI18N
	static final String LEAD_ENGINE = "leadEngine"; // NOI18N
	static final String STATUS = "status"; // NOI18N
	
	static final String CURRENT = "current"; // NOI18N
	static final String LEG2_START = "leg2Start"; // NOI18N
	static final String LEG3_START = "leg3Start"; // NOI18N
	static final String LEG2_END = "leg2End"; // NOI18N
	static final String LEG3_END = "leg3End"; // NOI18N
	static final String DEPARTURE_TRACK = "departureTrack"; // NOI18N
	static final String TERMINATION_TRACK = "terminationTrack"; // NOI18N
	
	static final String SCRIPTS = "scripts"; // NOI18N
	static final String AFTER_BUILD = "afterBuild"; // NOI18N
	static final String TERMINATE = "terminate"; // NOI18N
	static final String MOVE = "move"; // NOI18N
	
	static final String RAIL_ROAD = "railRoad"; // NOI18N
	static final String MANIFEST_LOGO = "manifestLogo"; // NOI18N
	static final String SHOW_TIMES = "showTimes"; // NOI18N
	
	// TrainManager.java
	static final String OPTIONS = "options"; // NOI18N
	static final String TRAIN_OPTIONS = "trainOptions"; // NOI18N
	static final String BUILD_MESSAGES = "buildMessages"; // NOI18N
	static final String BUILD_REPORT = "buildReport"; // NOI18N
	static final String PRINT_PREVIEW = "printPreview"; // NOI18N
	static final String OPEN_FILE = "openFile"; // NOI18N
	static final String TRAIN_ACTION = "trainAction"; // NOI18N
	
	static final String COLUMN_WIDTHS = "columnWidths"; // NOI18N TODO This here is for backwards compatibility, remove after next major release
	
	static final String TRAIN_SCHEDULE_OPTIONS = "trainScheduleOptions"; // NOI18N
	static final String ACTIVE_ID = "activeId"; // NOI18N
	static final String START_UP = "startUp"; // NOI18N
	static final String SHUT_DOWN = "shutDown"; // NOI18N

	// TrainManagerXml.java
	static final String TRAINS = "trains"; // NOI18N
	
	// TrainSchedule.java
	static final String SCHEDULE = "schedule"; // NOI18N
	static final String TRAIN_IDS = "trainIds"; // NOI18N
	
	// TrainScheduleManager.java
	static final String SCHEDULES = "schedules"; // NOI18N
	
	// ManifestCreator.java
	static final String MANIFEST_CREATOR = "manifestCreator"; // NOI18N
	static final String RUN_FILE = "runFile"; // NOI18N
	static final String DIRECTORY = "directory"; // NOI18N
	static final String COMMON_FILE = "commonFile"; // NOI18N
	
	// TrainManifestText.java
	static final String MANIFEST_TEXT_STRINGS = "manifestTextStrings"; // NOI18N
	static final String MANIFEST_FOR_TRAIN = "manifestForTrain"; // NOI18N
	static final String VALID = "valid"; // NOI18N
	static final String SCHEDULED_WORK = "scheduledWork"; // NOI18N
	static final String WORK_DEPARTURE_TIME = "workDepartureTime"; // NOI18N
	static final String WORK_ARRIVAL_TIME = "workArrivalTime"; // NOI18N
	static final String NO_SCHEDULED_WORK = "noScheduledWork"; // NOI18N
	static final String DEPART_TIME = "departTime"; // NOI18N
	static final String TRAIN_DEPARTS_CARS = "trainDepartsCars"; // NOI18N
	static final String TRAIN_DEPARTS_LOADS = "trainDepartsLoads"; // NOI18N
	static final String TRAIN_TERMINATES = "trainTerminates"; // NOI18N
	static final String DESTINATION = "destination"; // NOI18N
	static final String FROM = "from"; // NOI18N
	static final String TO = "to"; // NOI18N
	static final String DEST = "dest"; // NOI18N
	static final String FINAL_DEST = "finalDest"; // NOI18N
	static final String ADD_HELPERS = "addHelpers"; // NOI18N
	static final String REMOVE_HELPERS = "removeHelpers"; // NOI18N
	static final String LOCO_CHANGE = "locoChange"; // NOI18N
	static final String CABOOSE_CHANGE = "cabooseChange"; // NOI18N
	static final String LOCO_CABOOSE_CHANGE = "locoCabooseChange"; // NOI18N
	
	static final String TEXT = "text"; // NOI18N
	
	// TrainSwitchListText.java
	static final String SWITCH_LIST_TEXT_STRINGS = "switchListTextStrings"; // NOI18N
	static final String SWICH_LIST_FOR = "switchListFor"; // NOI18N
	static final String SCHEDULED_WORK_TRAIN= "scheduledWorkTrain"; // NOI18N
	
	static final String DEPARTS_AT = "departsAt"; // NOI18N
	static final String DEPARTS_EXPECTED_ARRIVAL = "departsExpectedArrival"; // NOI18N
	static final String DEPARTED_EXPECTED = "departedExpected"; // NOI18N
	
	static final String VISIT_NUMBER = "visitNumber"; // NOI18N
	static final String VISIT_NUMBER_DEPARTED = "visitNumberDeparted"; // NOI18N
	static final String VISIT_NUMBER_TERMINATES = "visitNumberTerminates"; // NOI18N
	static final String VISIT_NUMBER_TERMINATES_DEPARTED = "visitNumberTerminatesDeparted"; // NOI18N
	static final String VISIT_NUMBER_DONE = "visitNumberDone"; // NOI18N
	
	static final String TRAIN_DIRECTION_CHANGE = "trainDirectionChange"; // NOI18N
	static final String NO_CAR_PICK_UPS = "noCarPickUps"; // NOI18N
	static final String NO_CAR_SET_OUTS = "noCarSetOuts"; // NOI18N
	static final String TRAIN_DONE = "trainDone"; // NOI18N
	
}
