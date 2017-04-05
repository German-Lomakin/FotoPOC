Tools and libs:
	Android Studio 2.3
	Kotlin 1.1
	RXJava
	SugarORM
	Gson
How to run:
	1. Import project to Android Studio
	2. Run
	OR
	1. Download apk from FotoBuild folder
	2. Deploy
Assumptions:
	1. If user disable location permission or location is not available - (0, 0) location used by default
	2. for POI creation - please use long tap
	3. for change POI location - long tap and drag'n'drop marker on Detail Screen
	4. User can create POI everywhere
	5. Two different POI can be with the same name
	6. Gson import implemented in separated thread because initial gson could be much more bigger
	7. Remove POI function supported(Detaild Screen)
	8. Simple error handling
	9. Support both landscape and portrait orientations
	10. Min AndroidSDK version - 19

Time spent ~6h