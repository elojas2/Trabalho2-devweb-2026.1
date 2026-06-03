(function () {
    var form = document.getElementById('formLogin');
    if (!form) {
        return;
    }

    form.addEventListener('submit', function (e) {
        var email = document.getElementById('email').value.trim();
        var senha = document.getElementById('senha').value.trim();
        var msgErro = document.getElementById('msgErro');

        msgErro.style.display = 'none';
        msgErro.textContent = '';

        if (!email) {
            e.preventDefault();
            exibirErro(msgErro, 'Informe o e-mail.');
            return;
        }

        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            e.preventDefault();
            exibirErro(msgErro, 'Informe um e-mail válido.');
            return;
        }

        if (!senha) {
            e.preventDefault();
            exibirErro(msgErro, 'Informe a senha.');
            return;
        }

        if (senha.length < 6) {
            e.preventDefault();
            exibirErro(msgErro, 'A senha deve ter pelo menos 6 caracteres.');
        }
    });

    function exibirErro(el, msg) {
        el.textContent = msg;
        el.style.display = 'inline';
    }
})();

