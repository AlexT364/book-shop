	  document.addEventListener("DOMContentLoaded", function () {
	    const list = document.getElementById("author-checkbox-list");
	    const button = document.getElementById("load-more-authors");
	
	    let currentPage = 0;
	    const pageSize = 5;
	
	    const selectedAuthorIds = new URLSearchParams(window.location.search).getAll("authors");
	
	    function loadAuthors(pageNumber) {
	      fetch(`/api/authors?pageNumber=${pageNumber}&pageSize=${pageSize}`)
	        .then(response => {
	          if (!response.ok) throw new Error("Loading error");
	          return response.json();
	        })
	        .then(data => {
	          const authors = data.content;
	          if (authors.length === 0) {
	            button.disabled = true;
	            button.classList.add("disabled")
	            button.classList.add("btn-clr")
	            button.textContent = "No more authors";
	            return;
	          }
	
	          authors.forEach(author => {
	            const li = document.createElement("li");
	
	            const input = document.createElement("input");
	            input.type = "checkbox";
	            input.name = "authors";
	            input.value = author.id;
	            input.id = `filter-author-${author.id}`;
	            if (selectedAuthorIds.includes(author.id.toString())) {
	              input.checked = true;
	            }
	
	            const label = document.createElement("label");
	            label.textContent = author.name;
	            label.classList.add("filter-author");
	            label.setAttribute("for", input.id);
	
	            li.appendChild(input);
	            li.appendChild(label);
	            list.appendChild(li);
	          });
	
	          currentPage++;
	        })
	        .catch(error => {
	          button.disabled = true;
	          button.textContent = "Loading error";
	          console.error(error);
	        });
	    }
	
	    // Load first page
	    loadAuthors(currentPage);
	
	    // Button processor
	    button.addEventListener("click", function () {
	      loadAuthors(currentPage);
	    });
	  });
