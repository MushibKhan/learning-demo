document.addEventListener("DOMContentLoaded", function() {

	fetch("/sq/getForwardedCases")
		.then(response => response.json())
		.then(result => {

			if (!result.success || !result.data) {
				console.error("Failed to fetch data");
				return;
			}

			const tableBody = document.querySelector("#casesTable tbody");

			result.data.forEach(caseItem => {

				const row = document.createElement("tr");

				row.innerHTML = `
                    <td>${caseItem["SQ Case ID"]}</td>
                    <td>${caseItem["Reference No."]}</td>
                    <td>${caseItem["Association Name"]}</td>
                    <td>${caseItem["State"]}</td>
                    <td>${caseItem["Chief Functionary"]}</td>
                    <td>${caseItem["Email"]}</td>
                    <td>${caseItem["Generation Date"]}</td>
                    <td class="${getStatusClass(caseItem["Status"])}">
                        ${caseItem["Status"]}
                    </td>
                `;

				tableBody.appendChild(row);
			});

		})
		.catch(error => console.error("Error loading forwarded cases:", error));
});


function getStatusClass(status) {
	if (!status) return "";

	if (status.toUpperCase() === "DRAFT") {
		return "status-draft";
	}

	if (status.toUpperCase() === "CANCELLED") {
		return "status-cancelled";
	}

	return "";
}