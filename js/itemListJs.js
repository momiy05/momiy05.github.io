
window.onload = () => {
	let backflg = document.getElementById("flg").value;
	let backcolor = document.getElementById("view-color");
	if (backflg == "1") {
		backcolor.style.backgroundColor = "white";
	} else {
		backcolor.style.backgroundColor = "lightgreen";
	}
};
class updateButtonSwitch {
	flg = { name: "", serial: "", makerSerial: "", departmentId: "", placeId: "", cycle: "", updated: "" };
	constructor() {
		this.flg.name = document.getElementById("name").value;
		this.flg.serial = document.getElementById("serial").value;
		this.flg.makerSerial = document.getElementById("makerSerial").value;
		this.flg.departmentId = document.getElementById("departmentId").value;
		this.flg.placeId = document.getElementById("placeId").value;
		this.flg.cycle = document.getElementById("cycle").value;
		this.flg.updated = document.getElementById("updated").value;
		console.log(this.flg.name);
	}
	updateButton(emp) {
		switch (emp.name) {
			case "name":
				this.flg.name = emp.value;
				break;
			case "serial":
				this.flg.serial = emp.value;
				break;
			case "makerSerial":
				this.flg.makerSerial = emp.value;
				break;
			case "departmentId":
				this.flg.departmentId = emp.value;
				break;
			case "placeId":
				this.flg.placeId = emp.value;
				break;
			case "cycle":
				this.flg.cycle = emp.value;
				break;
			case "updated":
				this.flg.updated = emp.value;
				break;
		}
		let sum = 1;
		for (let vflg in this.flg) {
			sum *= this.flg[vflg].length;
			console.log(vflg + " " + this.flg[vflg].length + sum);
		}
		console.log(sum);
		if (sum > 0) {
			document.getElementById("send").disabled = false;
		} else {
			document.getElementById("send").disabled = true;

		}
	}
}

let upBtn = new updateButtonSwitch();

function orderSwitch() {
	let sortOrder = document.getElementById("sort");
	let searchOrder = document.getElementById("search");
	if (sortOrder.style.display == "inline-block") {
		sortOrder.style.display = "none";
		searchOrder.style.display = "inline-block";
	} else {
		sortOrder.style.display = "inline-block";
		searchOrder.style.display = "none";
	}
}
function optionSwitch() {
	let option = document.getElementById("option").value;

	let place = document.getElementById("placeId");
	let dep = document.getElementById("departmentId");
	if (option === "place") {
		place.style.display = "inline-block";
		dep.style.display = "none";
		dep.value = null;
	} else if (option === "department") {
		place.style.display = "none";
		place.value = null;
		dep.style.display = "inline-block";
	} else {
		place.style.display = "none";
		place.value = null;
		dep.style.display = "none";
		dep.value = null;
	}
}
function hamSwitch() {
	const ham = document.getElementById("ham");
	if (ham.style.display != "flex") {
		ham.style.display = "flex";
	} else {
		ham.style.display = "none";
	}
}
function changeColor(rowId) {
	let row = document.getElementById(rowId);
	if (row.style.backgroundColor == "red") {
		let backflg = document.getElementById("flg").value;
		if (backflg == "1") {
			row.style.backgroundColor = "white";
		} else {
			row.style.backgroundColor = "lightgreen";
		}
	} else {
		row.style.backgroundColor = "red";
	}
}