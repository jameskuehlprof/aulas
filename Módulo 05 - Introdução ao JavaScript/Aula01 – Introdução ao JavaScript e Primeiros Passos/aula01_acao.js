const REGRAS = {
    nome: {
        campo: 'nome',
        validar(valor) {
            if (!valor.trim()) return 'O nome é obrigatório.';
            if (valor.trim().length < 3) return 'O nome deve ter pelo menos 3 caracteres.';
            if (!/^[A-Za-zÀ-ÖØ-öø-ÿ\s]+$/.test(valor.trim())) return 'O nome deve conter apenas letras.';
            return null;
        }
    },
    email: {
        campo: 'email',
        validar(valor) {
            if (!valor.trim()) return 'O e-mail é obrigatório.';
            if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(valor.trim())) return 'Informe um e-mail válido.';
            return null;
        }
    },
    senha: {
        campo: 'senha',
        validar(valor) {
            if (!valor) return 'A senha é obrigatória.';
            if (valor.length < 6) return 'A senha deve ter pelo menos 6 caracteres.';
            return null;
        }
    },
    cidade: {
        campo: 'cidade',
        validar(valor) {
            if (!valor || valor === '') return 'Selecione uma cidade.';
            return null;
        }
    },
    sexo: {
        campo: 'sexo',
        validar() {
            const selecionado = document.querySelector('input[name="sexo"]:checked');
            if (!selecionado) return 'Selecione o sexo.';
            return null;
        }
    },
    interesse: {
        campo: 'interesse',
        validar() {
            const selecionados = document.querySelectorAll('input[name="interesse"]:checked');
            if (selecionados.length === 0) return 'Marque pelo menos um interesse.';
            return null;
        }
    }
};

function mostrarErro(nomeCampo, mensagem) {
    const erroEl = document.getElementById('erro-' + nomeCampo);
    const inputEl = document.querySelector('[name="' + nomeCampo + '"]') ||
                    document.querySelector('input[name="' + nomeCampo + '"]');

    if (erroEl) erroEl.textContent = mensagem || '';

    if (inputEl && !['sexo', 'interesse'].includes(nomeCampo)) {
        inputEl.classList.toggle('is-invalid', !!mensagem);
        inputEl.classList.toggle('is-valid', !mensagem);
    }
}

function validarCampo(regra) {
    let valor = null;
    const input = document.querySelector('[name="' + regra.campo + '"]');
    if (input) valor = input.value;

    const erro = regra.validar(valor);
    mostrarErro(regra.campo, erro);
    return !erro;
}

function validarTudo() {
    let tudo_ok = true;
    for (const chave in REGRAS) {
        const ok = validarCampo(REGRAS[chave]);
        if (!ok) tudo_ok = false;
    }
    return tudo_ok;
}

function configurarValidacaoAoSair() {
    ['nome', 'email', 'senha', 'cidade'].forEach(function(nomeCampo) {
        const el = document.querySelector('[name="' + nomeCampo + '"]');
        if (el) {
            el.addEventListener('blur', function() {
                validarCampo(REGRAS[nomeCampo]);
            });
            el.addEventListener('input', function() {
                if (el.classList.contains('is-invalid')) {
                    validarCampo(REGRAS[nomeCampo]);
                }
            });
        }
    });

    document.querySelectorAll('input[name="sexo"]').forEach(function(radio) {
        radio.addEventListener('change', function() {
            validarCampo(REGRAS.sexo);
        });
    });

    document.querySelectorAll('input[name="interesse"]').forEach(function(checkbox) {
        checkbox.addEventListener('change', function() {
            validarCampo(REGRAS.interesse);
        });
    });
}

function inicializar() {
    configurarValidacaoAoSair();

    const form = document.getElementById('formulario-cadastro');
    if (!form) return;

    form.addEventListener('submit', function(e) {
        e.preventDefault();

        const valido = validarTudo();

        if (valido) {
            const nome = document.querySelector('[name="nome"]').value.trim();
            const banner = document.getElementById('success-banner');
            if (banner) {
                banner.textContent = '✓ Cadastro de ' + nome + ' realizado com sucesso!';
                banner.classList.add('visible');
                form.reset();
                document.querySelectorAll('.is-valid, .is-invalid').forEach(function(el) {
                    el.classList.remove('is-valid', 'is-invalid');
                });
                setTimeout(function() { banner.classList.remove('visible'); }, 5000);
            }
        } else {
            const primeiro_erro = form.querySelector('.is-invalid');
            if (primeiro_erro) {
                primeiro_erro.scrollIntoView({ behavior: 'smooth', block: 'center' });
                primeiro_erro.focus();
            }
        }
    });

    form.addEventListener('reset', function() {
        setTimeout(function() {
            document.querySelectorAll('.is-valid, .is-invalid').forEach(function(el) {
                el.classList.remove('is-valid', 'is-invalid');
            });
            document.querySelectorAll('.field-error').forEach(function(el) {
                el.textContent = '';
            });
        }, 0);
    });
}

document.addEventListener('DOMContentLoaded', inicializar);
