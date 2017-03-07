/**
 *  Track Battery Levels
 *
 *  Author: csmith@dreadnought.org
 *  Date: 2013-11-16
 */
preferences {
	section("Monitor battery levels for these devices...") {
		input "batteries", "capability.battery", multiple: true
	}
    section("A notification will be sent when the battery is 'n'% full (default 10%)") {
        input "threshold", "number", required: false
    }
}

def installed() {
    state.threshold = threshold ? threshold as int : 10
	initialize()
}

def updated() {
	unschedule()
    state.threshold = threshold ? threshold as int : 10
	initialize()
}

def initialize() {
    schedule("0 0 0,12 * * ?", checkLevels)
}

def checkLevels() {
	batteries.each { battery ->
        if (battery.currentBattery < state.threshold) {
            sendPush("Battery ${battery.displayName} is at ${battery.currentBattery}%")
        }
    }
}  
