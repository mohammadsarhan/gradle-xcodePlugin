package org.openbakery.output

import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.openbakery.Destination
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

/**
 * Created with IntelliJ IDEA.
 * User: rene
 * Date: 09.10.13
 * Time: 18:19
 * To change this template use File | Settings | File Templates.
 */
class TestBuildOutputAppenderTest {



	def errorTestOutput = "Test Case '-[DTActionPanelTest_iPhone testActionPanelSizeDidChangeDelegate]' started.\n" +
					"2013-10-09 18:12:12:101 FOO[22741:c07] layoutSubviews\n" +
					"2013-10-09 18:12:12:101 FOO[22741:c07] oldFrame {{0, 380}, {320, 80}}\n" +
					"2013-10-09 18:12:12:102 FOO[22741:c07] newFrame {{0, 320}, {320, 140}}\n" +
					"/Users/dummy/poject/UnitTests/iPhone/DTPopoverController/DTActionPanelTest_iPhone.m:85: error: -[DTActionPanelTest_iPhone testActionPanelSizeDidChangeDelegate] : Expected 2 matching invocations, but received 0\n" +
					"Test Case '-[DTActionPanelTest_iPhone testActionPanelSizeDidChangeDelegate]' failed (0.026 seconds).\n" +
					"Test Case '-[DTActionPanelTest_iPhone testActionPanelSizeDidChangeDelegateOnHide]' started."


	def successTestOutput = "Test Case '-[DTActionPanelTest_iPhone testCollapsed]' started.\n" +
					"2013-10-09 18:12:12:108 FOO[22741:c07] newFrame {{0, 320}, {320, 140}}\n" +
					"2013-10-09 18:12:12:112 FOO[22741:c07] empty\n" +
					"2013-10-09 18:12:12:113 FOO[22741:c07] empty\n" +
					"Test Case '-[DTActionPanelTest_iPhone testCollapsed]' passed (0.005 seconds)."


	Project project

	@BeforeClass
	def setup() {
		project = ProjectBuilder.builder().build()
		project.buildDir = new File('build').absoluteFile
		project.apply plugin: org.openbakery.XcodePlugin

		Destination destinationPad = new Destination()
		destinationPad.platform = "iPhoneSimulator"
		destinationPad.name = "iPad"
		destinationPad.arch = "i386"
		destinationPad.id = "iPad Air"
		destinationPad.os = "iOS"

		Destination destinationPhone = new Destination()
		destinationPhone.platform = "iPhoneSimulator"
		destinationPhone.name = "iPhone"
		destinationPhone.arch = "i386"
		destinationPhone.id = "iPhone 4s"
		destinationPhone.os = "iOS"


		project.xcodebuild.availableSimulators << destinationPad
		project.xcodebuild.availableSimulators << destinationPhone

		project.xcodebuild.destination {
			name = "iPad"
		}
		project.xcodebuild.destination {
			name = "iPhone"
		}

	}

	@Test
	void testNoOutput() {
		StyledTextOutputStub output = new StyledTextOutputStub()

		TestBuildOutputAppender appender = new TestBuildOutputAppender(output, project)

		appender.append("PhaseScriptExecution Copy\\ Pods\\ Resources build/obj/MyApp.build/Debug-iphonesimulator/myApp.build/Script-FCB0D86122C34DC69AE16EE3.sh")

		assert output.toString().equals("") : "Expected empty output but was " + output

	}

	@Test
	void testSuccess() {

		StyledTextOutputStub output = new StyledTextOutputStub()

		TestBuildOutputAppender appender = new TestBuildOutputAppender(output, project)
		appender.append("PhaseScriptExecution Copy\\ Pods\\ Resources build/obj/MyApp.build/Debug-iphonesimulator/myApp.build/Script-FCB0D86122C34DC69AE16EE3.sh")

		for (String line in successTestOutput.split("\n")) {
				appender.append(line)
		}
		String expected = "\nPerform unit tests for: iPad/iPhoneSimulator/iOS\n\n      OK -[DTActionPanelTest_iPhone testCollapsed] - (0.005 seconds)\n"
		assert output.toString().equals(expected) : "Expected '" + expected + "' but was: " + output.toString()
	}

	@Test
	void testFailed() {

		StyledTextOutputStub output = new StyledTextOutputStub()

		TestBuildOutputAppender appender = new TestBuildOutputAppender(output, project)
		appender.append("PhaseScriptExecution Copy\\ Pods\\ Resources build/obj/MyApp.build/Debug-iphonesimulator/myApp.build/Script-FCB0D86122C34DC69AE16EE3.sh")

		for (String line in errorTestOutput.split("\n")) {
				appender.append(line)
		}
		String expected = "\nPerform unit tests for: iPad/iPhoneSimulator/iOS\n\n  FAILED -[DTActionPanelTest_iPhone testActionPanelSizeDidChangeDelegate] - (0.026 seconds)\n"
		assert output.toString().equals(expected) : "Expected '" + expected + "' but was: " + output.toString()
	}




	@Test
	void testFinished() {
		String simctlOutput = FileUtils.readFileToString(new File("src/test/Resource/xcodebuild-output.txt"))

		StyledTextOutputStub output = new StyledTextOutputStub()

		TestBuildOutputAppender appender = new TestBuildOutputAppender(output, project)

		for (String line : simctlOutput.split("\n")) {
			appender.append(line);
		}

		assert output.toString().contains("Tests finished:")

	}

	@Test
	void testFinishedFailed() {
		String simctlOutput = FileUtils.readFileToString(new File("src/test/Resource/xcodebuild-output-test-failed.txt"))

		StyledTextOutputStub output = new StyledTextOutputStub()

		TestBuildOutputAppender appender = new TestBuildOutputAppender(output, project)

		for (String line : simctlOutput.split("\n")) {
			appender.append(line);
		}

		assert output.toString().contains("TESTS FAILED")

	}

	@Test
	void testComplexOutput() {
		String simctlOutput = FileUtils.readFileToString(new File("src/test/Resource/xcodebuild-output-complex-test.txt"))
		StyledTextOutputStub output = new StyledTextOutputStub()
		TestBuildOutputAppender appender = new TestBuildOutputAppender(output, project)
		for (String line : simctlOutput.split("\n")) {
			appender.append(line);
		}
		assert output.toString().contains("Perform unit tests for: iPad/iPhoneSimulator/iOS")
		assert output.toString().contains("Tests finished: iPad/iPhoneSimulator/iOS")
		assert output.toString().contains("Perform unit tests for: iPhone/iPhoneSimulator/iOS")
		assert output.toString().contains("Tests finished: iPhone/iPhoneSimulator/iOS")
	}

}
