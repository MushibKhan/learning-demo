document.addEventListener("DOMContentLoaded", function() {

	tinymce.init({
		selector: '#editor',
		height: 300,
		license_key: 'gpl',
		menubar: false,
		plugins: 'lists link image table code',
		toolbar: 'undo redo | bold italic underline | alignleft aligncenter alignright | bullist numlist | link',
		branding: false,
		statusbar: false
	});

	const moduleSelect = document.getElementById("moduleSelect");
	const previousDiv = document.getElementById("previousDrafts");
	const draftDetailsDiv = document.getElementById("draftDetails");

	moduleSelect.addEventListener("change", function() {

		let moduleId = this.value;
		let editor = tinymce.get("editor");

		if (!moduleId) {
			if (editor) editor.setContent('');
			previousDiv.innerHTML = '<strong>Previous Drafts:</strong><div><i>No drafts yet</i></div>';
			draftDetailsDiv.innerHTML = '<strong>Draft Details:</strong><div><i>Select a draft to view details</i></div>';
			return;
		}

		fetch("/api/draft", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ moduleId: moduleId })
		})
			.then(response => response.json())
			.then(data => {

				if (editor) editor.setContent(data.latestDraft?.description || '');

				draftDetailsDiv.innerHTML =
					'<strong>Draft Details:</strong><div><i>Select a draft to view details</i></div>';

				previousDiv.innerHTML = '<strong>Previous Drafts:</strong>';

				if (data.previousDrafts && data.previousDrafts.length > 0) {
					data.previousDrafts.forEach(draft => {

						const draftElem = document.createElement('div');

						draftElem.innerHTML = `
                        <span class="draft-link">
                            ${draft.fileNo || 'N/A'} - ${draft.moduleId || 'N/A'}
                        </span>
                    `;

						draftElem.querySelector('.draft-link')
							.addEventListener('click', () => {
								draftDetailsDiv.innerHTML = `
                                <strong>File No:</strong> ${draft.fileNo || 'N/A'} <br>
                                <strong>Module ID:</strong> ${draft.moduleId || 'N/A'} <br>
                                <strong>Entry Date:</strong> ${draft.entryDate || 'N/A'} <br><br>
                                <strong>Draft Description:</strong> ${draft.description || ''}
                            `;
							});

						previousDiv.appendChild(draftElem);
					});

				} else {
					previousDiv.innerHTML += '<div><i>No previous drafts</i></div>';
				}
			})
			.catch(error => console.error("Error fetching module content:", error));
	});

	document.getElementById("saveBtn").addEventListener("click", function() {

		let moduleId = moduleSelect.value;

		if (!moduleId) {
			alert("Please select a module first!");
			return;
		}

		let editor = tinymce.get("editor");
		let content = editor.getContent();

		fetch("/api/draft/save", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ moduleId: moduleId, description: content })
		})
			.then(response => response.json())
			.then(data => {
				alert("Saved successfully! Module: " + data.moduleId);
				moduleSelect.dispatchEvent(new Event('change'));
			})
			.catch(error => console.error("Error saving content:", error));
	});

});