import groovy.json.JsonSlurper





static void main(String[] args) {

  println "Hello world!"

  // Specify the starting directory for the scan
  def startDir = new File(args[0])

  processDirectory(startDir)

  println "Found ${Globals.jsonCollection.size()} weight entries."

// Access and process data within the collection
  Globals.jsonCollection.each { map ->
    println "Data: ${map}" // You can access individual elements from the map here
    weightValue = new WeightValue()

    if(map.containsKey(WeightKeys.date)) {
      weightValue.date = map[WeightKeys.date]
    }
    if(map.containsKey(WeightKeys.fat)) {
      weightValue.fat = map[WeightKeys.fat]
    }
    if(map.containsKey(WeightKeys.weight)) {
      weightValue.weight = map[WeightKeys.weight]
    }
    if(map.containsKey(WeightKeys.logId)) {
      weightValue.logId = map[WeightKeys.logId]
    }
    if(map.containsKey(WeightKeys.time)) {
      weightValue.time = map[WeightKeys.time]
    }
    if(map.containsKey(WeightKeys.source)) {
      weightValue.source = map[WeightKeys.source]
    }
    if(map.containsKey(WeightKeys.bmi)) {
      weightValue.bmi = map[WeightKeys.bmi]
    }
    //Unique Keys: [date, fat, weight, logId, time, source, bmi] across all of the json files.
    weightValues.add(weightValue)
  }

  def allKeys = new HashSet<String>()

  Globals.jsonCollection.each { map ->
    allKeys.addAll(map.keySet()) // Add all keys from each map to the set
  }

  def uniqueKeys = allKeys.toList().unique() // Convert set to list and get unique elements

  println "Unique Keys: ${uniqueKeys}"

  println WeightKeys.toCsv()

}

def processDirectory(File dir) {
  dir.eachFile { file ->
    if (file.isDirectory()) {
      processDirectory(file) // Recursively call for subdirectories
    } else if (file.name.endsWith(Globals.jsonExtension)) {
      // Parse JSON and convert to desired structure
      def jsonData = new JsonSlurper().parse(file)
      convertJsonToMap(jsonData) // Add parsed data to collection
    }
  }
}

// This method can be customized to fit your specific JSON structure
//static def convertJsonToMap(data) {
//  // Modify this logic to extract desired data and create your map structure
//  def map = [:]
//  // Example: Extract key-value pairs from the first level of the JSON object
//  data.each { key, value -> map[key] = value }
//  return map
//}

def convertJsonToMap(data) {
  def map = [:]
  try {
    // Attempt to iterate through data as a map
    data.each { key, value -> map[key] = value }
  } catch (ignored) {
    // Handle cases where data isn't a map
    if (data instanceof List) {
      // If data is a list, iterate through its elements
        for (item in data) {
          Globals.jsonCollection << convertJsonToMap(item)
        } // Recursively convert list elements
    } else {
      // Handle other data types (optional - customize based on your needs)
      println "Skipping non-map/list data: ${data}"
    }
  }
  return map
}

class Globals {
  static String jsonExtension = '.json'
  static ArrayList jsonCollection = []
  static ArrayList<WeightValue> weightValues = []
}


//Unique Keys: [date, fat, weight, logId, time, source, bmi] across all of the json files.

class WeightKeys {
  static String logId = 'logId'
  static String date = 'date'
  static String time = 'time'
  static String weight = 'weight'
  static String bmi = 'bmi'
  static String fat = 'fat'
  static String source = 'source'

    static String toCsv() {
    // Build the CSV string with property values separated by commas
    return "${logId}, ${date}, ${time}, ${weight}, ${bmi}, ${fat}, ${source}"
  }
}

class WeightValue {
  String logId = ""
  String date = ""
  String time = ""
  String weight = ""
  String bmi = ""
  String fat = ""
  String source = ""

  static String toCsv() {
    // Build the CSV string with property values separated by commas
    return "${logId}, ${date}, ${time}, ${weight}, ${bmi}, ${fat}, ${source}"
  }
}