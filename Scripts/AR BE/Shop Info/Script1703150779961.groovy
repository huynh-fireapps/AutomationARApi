import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import groovy.json.JsonSlurper as JsonSlurper
import com.kms.katalon.core.util.KeywordUtil
import java.sql.Connection
import java.sql.ResultSet

import com.kms.katalon.core.configuration.RunConfiguration

import com.katalon.plugin.keyword.connection.DBType


// Send the first request
def authResponse = sendRequestAndHandleResponse('AR BE/Auth', [('shop') : raw_domain])
def authorization = extractAuthorizationToken(authResponse)

// Send the second request
def shopInfoResponse = sendRequestAndHandleResponse('AR BE/Shop Info', [('authorization') : authorization])

// Parse and log the shop name
def shopInfo = new JsonSlurper().parseText(shopInfoResponse.getResponseBodyContent())
KeywordUtil.logInfo(shopInfo.data.shopInfo.shop_name)

def extractAuthorizationToken(response) {
	def jsonResponse = new JsonSlurper().parseText(response.getResponseBodyContent())
	return 'Bearer ' + jsonResponse.data.token
}

// Function to send request and handle response
def sendRequestAndHandleResponse(testObject, additionalParams = [:]) {
	def response = WS.sendRequest(findTestObject(testObject, additionalParams))
	WS.verifyResponseStatusCode(response, 200)
	WS.validateJsonAgainstSchema(null, null, FailureHandling.STOP_ON_FAILURE)
	return response
}

