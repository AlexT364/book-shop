document.getElementById('cartForm').addEventListener('submit', function(event) {
    event.preventDefault();
    document.getElementById('quantityCart').value = document.getElementById('bookQuantity').value;
    this.submit();
});