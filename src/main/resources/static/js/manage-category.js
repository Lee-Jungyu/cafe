function showModal(option) {
    switch (option) {
        case 'create':
            var modal = document.getElementsByClassName('modal-background')[0];
            var modalContent = document.getElementsByClassName('modal-add-category')[0];

            modalContent.style.display = 'block';
            modal.style.display = 'block';

            break;
        case 'update':
            var modal = document.getElementsByClassName('modal-background')[0];
            var modalContent = document.getElementsByClassName('modal-edit-category')[0];

            modalContent.style.display = 'block';
            modal.style.display = 'block';

            break;
        case 'delete':
            var modal = document.getElementsByClassName('modal-background')[0];
            var modalContent = document.getElementsByClassName('modal-remove-category')[0];

            modalContent.style.display = 'block';
            modal.style.display = 'block';

            break;
        case 'inquiry':

            break;
    }
}