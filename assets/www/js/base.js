/**
 * Copyright 2013 Carl-Philipp Harmant
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

function search(tab, str) {
	var i = 0;
	for (i; i < tab.length; i++) {
		if (str == tab[i]) {
			return i;
			break;
		}
	}
}
function getExplode(tab) {
	if (tab.length < 5) {
		return 5;
	} else {
		return 10;
	}
}
function poufpouf(id) {
	var style = document.getElementById(id).style.display;
	if (id.indexOf(".") != -1) {
		id = id.replace(".", "\\.");
	}

	if (style == "none")
		$("#" + id).show("fast");
	else
		$("#" + id).hide("fast");
}