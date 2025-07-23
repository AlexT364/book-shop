 	  document.addEventListener("DOMContentLoaded", function () {
 	    const list = document.getElementById("genre-checkbox-list");
 	    const button = document.getElementById("load-more-genres");
	
 	    let currentPage = 0;
 	    const pageSize = 5;
	
 	    const selectedGenreIds = new URLSearchParams(window.location.search).getAll("genres");
	
 	    function loadGenres(pageNumber) {
 	      fetch(`/api/genres?pageNumber=${pageNumber}&pageSize=${pageSize}`)
 	        .then(response => {
 	          if (!response.ok) throw new Error("Loading error");
 	          return response.json();
 	        })
 	        .then(data => {
 	          const genres = data.content;
 	          if (genres.length === 0) {
 	            button.disabled = true;
 	            button.classList.add("disabled")
 	            button.classList.add("btn-clr")
 	            button.textContent = "No more genres";
 	            return;
 	          }
	
 	          genres.forEach(genre => {
 	            const li = document.createElement("li");
	
 	            const input = document.createElement("input");
 	            input.type = "checkbox";
 	            input.name = "genres";
 	            input.value = genre.id;
 	            input.id = `filter-genre-${genre.id}`;
 	            if (selectedGenreIds.includes(genre.id.toString())) {
 	              input.checked = true;
 	            }
	
 	            const label = document.createElement("label");
 	            label.textContent = genre.name;
 	            label.classList.add("filter-genre");
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
 	    loadGenres(currentPage);
	
 	    // Button processor
 	    button.addEventListener("click", function () {
 	      loadGenres(currentPage);
 	    });
 	  });