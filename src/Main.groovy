import groovy.json.JsonSlurper





static void main(String[] args) {

  println "Hello world!"

  // Specify the starting directory for the scan
  def startDir = new File(args[0])

  processDirectory(startDir)

  println "Found ${Globals.jsonCollection.size()} JSON files."

// Access and process data within the collection
  Globals.jsonCollection.each { map ->
    println "Data: ${map}" // You can access individual elements from the map here

  }
}

def processDirectory(File dir) {
  dir.eachFile { file ->
    if (file.isDirectory()) {
      processDirectory(file) // Recursively call for subdirectories
    } else if (file.name.endsWith(Globals.jsonExtension)) {
      // Parse JSON and convert to desired structure
      def jsonData = new JsonSlurper().parse(file)
      Globals.jsonCollection << convertJsonToMap(jsonData) // Add parsed data to collection
    }
  }
}

// This method can be customized to fit your specific JSON structure
static def convertJsonToMap(data) {
  // Modify this logic to extract desired data and create your map structure
  def map = [:]
  // Example: Extract key-value pairs from the first level of the JSON object
  data.each { key, value -> map[key] = value }
  return map
}

class Globals {
  static String jsonExtension = '.json'
  static ArrayList jsonCollection = []
}
