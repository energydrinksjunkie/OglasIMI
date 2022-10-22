document.querySelector('#username').addEventListener('blur', validateUsername);
document.querySelector('#password').addEventListener('blur', validatePassword);
document.querySelector('#email').addEventListener('blur', validateEmail);
document.querySelector('#korisnikMob').addEventListener('blur', validateMob);
document.querySelector('#korisnikRodjen').addEventListener('blur', validateRodjen);

document.querySelector('#korisnikIme').addEventListener('blur', validateIme);
document.querySelector('#korisnikPrezime').addEventListener('blur', validatePrezime);
document.querySelector('#korisnikBio').addEventListener('blur', validateBio);

const notEmpty = /.{1,255}/;
const removeSpaces = /^\S*$/;
// - can not have spaces

function validateUsername(e) {
    const idContainer = document.querySelector('#username');
    const re = /^([A-Za-z0-9]){4,20}$/;
    // - Value must be from 4 to 20 characters in length, 
    // - only allow letters and numbers, no special characters, 
    // - full line is evaluated.

    if (re.test(idContainer.value) && removeSpaces.test(idContainer.value)) {
        idContainer.classList.remove('is-invalid');
        idContainer.classList.add('is-valid');
        return true;
    }

    else {
        idContainer.classList.remove('is-valid');
        idContainer.classList.add('is-invalid');
        return false;
    }
}

function validatePassword(e) {
    const re = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/;
    // - at least 8 characters
    // - must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number
    // - Can contain special characters
    const idContainer = document.querySelector('#password');

    if (re.test(idContainer.value) && removeSpaces.test(idContainer.value)) {
        idContainer.classList.remove('is-invalid');
        idContainer.classList.add('is-valid');
        return true;
    }

    else {
        idContainer.classList.remove('is-valid');
        idContainer.classList.add('is-invalid');
        return false;
    }
}

function validateEmail(e) {
    const re = /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
    // - Email Validation as per RFC2822 standards.
    // - Straight from .net helpfiles :)

    const idContainer = document.querySelector('#email');

    if (re.test(idContainer.value) && removeSpaces.test(idContainer.value)) {
        idContainer.classList.remove('is-invalid');
        idContainer.classList.add('is-valid');
        return true;
    }

    else {
        idContainer.classList.remove('is-valid');
        idContainer.classList.add('is-invalid');
        return false;
    }
}

function validateMob(e) {
    const re = /^\s*(?:\+?(\d{1,3}))?([-. (]*(\d{3})[-. )]*)?((\d{3})[-. ]*(\d{2,4})(?:[-.x ]*(\d+))?)\s*$/;
    // - Positive: +42 555.123.4567 +1-(800)-123-4567 +7 555 1234567 +7(926)1234567 
    // - (926) 1234567 +79261234567 926 1234567 9261234567 1234567 123-4567 123-89-01 495 123

    const idContainer = document.querySelector('#korisnikMob');

    if (re.test(idContainer.value)) {
        idContainer.classList.remove('is-invalid');
        idContainer.classList.add('is-valid');
        return true;
    }

    else {
        idContainer.classList.remove('is-valid');
        idContainer.classList.add('is-invalid');
        return false;
    }
}

function validateRodjen(e) {
    const re = /([\d]+)([\-\./])([\d]+)([\-\./])([\d]+)|((Jan(|uary)|Feb(|ruary)|Mar(|ch)|Apr(|il)|May|Jun(|e)|Jul(|y)|Aug(|ust)|Sept(|ember)|Oct(|ober)|(Nov|Dec)(|ember))([\s\-])(|([\d]+){1,2}([\s\-]|\, ))([\d]+){4})/;
    // -  Date formatting in these formats:
    // - 01-01-2011
    // - 01/01/2011
    // - Jan 1, 2011
    // - Jan 01, 2011
    const idContainer = document.querySelector('#korisnikRodjen');

    if (re.test(idContainer.value)) {
        idContainer.classList.remove('is-invalid');
        idContainer.classList.add('is-valid');
        return true;
    }

    else {
        idContainer.classList.remove('is-valid');
        idContainer.classList.add('is-invalid');
        return false;
    }
}

function validateIme(e) {
    const idContainer = document.querySelector('#korisnikIme');

    if (notEmpty.test(idContainer.value)) {
        idContainer.classList.remove('is-invalid');
        idContainer.classList.add('is-valid');
        return true;
    }

    else {
        idContainer.classList.remove('is-valid');
        idContainer.classList.add('is-invalid');
        return false;
    }
}

function validatePrezime(e) {
    const idContainer = document.querySelector('#korisnikPrezime');

    if (notEmpty.test(idContainer.value)) {
        idContainer.classList.remove('is-invalid');
        idContainer.classList.add('is-valid');
        return true;
    }

    else {
        idContainer.classList.remove('is-valid');
        idContainer.classList.add('is-invalid');
        return false;
    }
}

function validateBio(e) {
    const idContainer = document.querySelector('#korisnikBio');
    const re = /.{1,2000}/;


    if (re.test(idContainer.value)) {
        idContainer.classList.remove('is-invalid');
        idContainer.classList.add('is-valid');
        return true;
    }

    else {
        idContainer.classList.remove('is-valid');
        idContainer.classList.add('is-invalid');
        return false;
    }
}

(function () {
    const forms = document.querySelectorAll('.needs-validation');

    for (let form of forms) {
        form.addEventListener(
            'submit',
            function (event) {
                if (
                    !validateUsername() ||
                    !validatePassword() ||
                    !validateEmail() ||
                    !validateMob() ||
                    !validateRodjen() ||
                    !validateIme() ||
                    !validatePrezime() ||
                    !validateBio()
                ) {
                    event.preventDefault();
                    event.stopPropagation();
                } else {
                    // form.classList.add('was-validated');
                }
            },
            false
        );
    }
})();