document.getElementById('cartForm').addEventListener('submit', function(event) {
	    const quantity = document.getElementById('bookQuantity').value;
	    document.getElementById('quantityCart').value = quantity || 1;
});