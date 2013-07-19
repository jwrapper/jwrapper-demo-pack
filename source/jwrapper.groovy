import groovy.io.FileType

def basedir = project.basedir

def properties = project.properties

def dependencies = properties["dependencies"]
println "dependencies=${dependencies}"

def dependencyList = []

new File(dependencies)
		.eachFileRecurse(FileType.FILES) { file ->
			if(file.name.endsWith('.jar')) {
				dependencyList << file.getAbsolutePath()
			}
		}

dependencyList.each{file -> println "\t dependency=${file}" }

def binding = [:]
binding << properties
binding << [basedir: basedir]
binding << [dependencyList: dependencyList]

def wrapperSource = properties["wrapperTemplateSource"]
println "wrapperSource=${wrapperSource}"

def jwrapper = new File(wrapperSource).text
def engine = new groovy.text.XmlTemplateEngine()
def template = engine.createTemplate(jwrapper).make(binding)

def wrapperTarget = properties["wrapperTemplateTarget"]
println "wrapperTarget=${wrapperTarget}"

new File(wrapperTarget).write(template.toString())
