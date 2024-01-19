function updateGameNumbers() {
    var visibleRows = document.querySelectorAll('.game-row:not(.hidden)');
    visibleRows.forEach(function(row, index) {
        var numberCell = row.querySelector('.game-number');
        numberCell.textContent = index + 1;
    });
}

function show(status) {
    var allRows = document.querySelectorAll('.game-row');
    var gamesContainer = document.querySelector('#games-list tbody');
    var firstGameRow = gamesContainer.querySelector('.game-row');

    var matchingRows = [];
    allRows.forEach(function(row) {
        if (row.dataset.status === status || status === 'all') {
            matchingRows.push(row);
        } else {
            row.classList.add('hidden');
        }
    });

    matchingRows.forEach(function(row) {
        row.classList.remove('hidden');
    });
    updateGameNumbers();
}


