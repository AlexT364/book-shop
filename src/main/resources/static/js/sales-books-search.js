const searchInput = document.getElementById("bookSearch");
const resultsList = document.getElementById("bookResults");
const selectedList = document.getElementById("selectedBooks");
const hiddenInput = document.getElementById("bookIds");

let selectedBooks = [];

searchInput.addEventListener("input", async () => {
    const query = searchInput.value.trim();
    if (query.length < 2) {
        resultsList.innerHTML = "";
        return;
    }

    const response = await fetch(`/admin/api/books/search?query=${query}`);
    const books = await response.json();

    resultsList.innerHTML = "";
    books.forEach(book => {
        const li = document.createElement("li");
        li.textContent = book.title;
        li.addEventListener("click", () => addBook(book));
        resultsList.appendChild(li);
    });
});

function addBook(book) {
    if (selectedBooks.find(b => b.id === book.id)) return;

    selectedBooks.push(book);
    renderSelected();
    updateHiddenField();
}

function removeBook(bookId) {
    selectedBooks = selectedBooks.filter(b => b.id !== bookId);
    renderSelected();
    updateHiddenField();
}

function renderSelected() {
    selectedList.innerHTML = "";
    selectedBooks.forEach(book => {
        const li = document.createElement("li");
        li.textContent = book.title + " ";
        const removeBtn = document.createElement("button");
        removeBtn.textContent = "x";
        removeBtn.addEventListener("click", () => removeBook(book.id));
        li.appendChild(removeBtn);
        selectedList.appendChild(li);
    });
}

function updateHiddenField() {
    hiddenInput.value = selectedBooks.map(b => b.id).join(",");
}