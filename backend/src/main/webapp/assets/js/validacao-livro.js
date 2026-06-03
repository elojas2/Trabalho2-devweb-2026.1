(function () {
    var form = document.getElementById('formLivro');
    if (!form) return;

    form.addEventListener('submit', function (e) {
        var titulo  = document.getElementById('titulo').value.trim();
        var autor   = document.getElementById('autor').value.trim();
        var ano     = document.getElementById('ano').value.trim();
        var msgErro = document.getElementById('msgErro');

        // limpa erro anterior
        msgErro.style.display = 'none';
        msgErro.textContent = '';

        if (titulo.length < 2) {
            e.preventDefault();
            exibirErro(msgErro, 'Título deve ter pelo menos 2 caracteres.');
            return;
        }

        if (autor.length < 2) {
            e.preventDefault();
            exibirErro(msgErro, 'Autor deve ter pelo menos 2 caracteres.');
            return;
        }

        var anoNum = parseInt(ano, 10);
        if (isNaN(anoNum) || anoNum < 1000 || anoNum > new Date().getFullYear()) {
            e.preventDefault();
            exibirErro(msgErro, 'Ano deve ser um número entre 1000 e ' + new Date().getFullYear() + '.');
            return;
        }

        // previne injeção básica de HTML/script
        if (/<[^>]*script/i.test(titulo) || /<[^>]*script/i.test(autor)) {
            e.preventDefault();
            exibirErro(msgErro, 'Caracteres inválidos detectados.');
            return;
        }
    });

    function exibirErro(el, msg) {
        el.textContent = msg;
        el.style.display = 'inline';
    }
})();
