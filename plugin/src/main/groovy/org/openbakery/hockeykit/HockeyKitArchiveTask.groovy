/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openbakery.hockeykit

import org.gradle.api.tasks.TaskAction

class HockeyKitArchiveTask extends AbstractHockeyKitTask {

	HockeyKitArchiveTask() {
		super()
		dependsOn("archive")
		this.description = "Prepare the app bundle so that it can be uploaded to the Hockeykit Server"
	}


	@TaskAction
	def archive() {
		if (project.hockeykit.versionDirectoryName == null) {
			throw new IllegalArgumentException("hockeykit.versionDirectoryName is missing")
		}


		copyIpaToDirectory(getOutputDirectory());


	}
}
