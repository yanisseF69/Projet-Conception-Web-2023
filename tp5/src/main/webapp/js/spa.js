/**
 * Placez ici les scripts qui seront exécutés côté client pour rendre l'application côté client fonctionnelle.
 */

// <editor-fold desc="Gestion de l'affichage">
var userAuth = {
    login: '',
    name: '',
    password: '',
    authorisation:'',
    isAuthenticated: false,
    todoAssigned: [],
}
var todosRefresh;

/**
 * Fait basculer la visibilité des éléments affichés quand le hash change.<br>
 * Passe l'élément actif en inactif et l'élément correspondant au hash en actif.
 * @param hash une chaîne de caractères (trouvée a priori dans le hash) contenant un sélecteur CSS indiquant un élément à rendre visible.
 */
function show(hash) {
    const oldActiveElement = document.querySelector(".active");
    oldActiveElement.classList.remove("active");
    oldActiveElement.classList.add("inactive");
    const newActiveElement = document.querySelector(hash.split("/")[0]);
    newActiveElement.classList.remove("inactive");
    newActiveElement.classList.add("active");
}


/**
 * Fetch the users.
 */
async function fetchUsers() {
    const headers = new Headers();
    headers.append("Accept", "application/json");
    const requestConfig = {
        method: "GET",
        headers: headers,
        mode: "cors" // pour le cas où vous utilisez un serveur différent pour l'API et le client.
    };

        try {
            const response = await fetch(baseUrl + "users",  requestConfig);
            return await response.json();
        } catch (e) {
            console.error("In fetchUsers() : " + e);
        }

}


// Fonction pour mettre à jour les informations de l'utilisateur
function modifyUser(name='', password='') {
    const body = {
        name: name !== '' ? name : userAuth.name,
        password: password !== '' ? password : userAuth.password,
    }

    const headers = new Headers();
    headers.append("Accept", "*/*");
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", userAuth.authorisation);
    const requestConfig = {
        method: "PUT",
        headers: headers,
        body: JSON.stringify(body),
        mode: "cors" // pour le cas où vous utilisez un serveur différent pour l'API et le client.
    };

    fetch(baseUrl + "users/" + userAuth.login, requestConfig)
        .then(response => {
            userAuth.name = body.name;
            userAuth.password = body.password;
            displayRequestResult("Modification réussie", "alert-success");
            renderAccount();
            renderName();
        });

}


/**
 * Affiche pendant 10 secondes un message sur l'interface indiquant le résultat de la dernière opération.
 * @param text Le texte du message à afficher
 * @param cssClass La classe CSS dans laquelle afficher le message (défaut = alert-info)
 */
function displayRequestResult(text, cssClass = "alert-info") {
    const requestResultElement = document.getElementById("requestResult");
    requestResultElement.innerText = text;
    requestResultElement.classList.add(cssClass);
    setTimeout(
        () => {
            requestResultElement.classList.remove(cssClass);
            requestResultElement.innerText = "";
        }, 10000);
}

/**
 * Affiche ou cache les éléments de l'interface qui nécessitent une connexion.
 * @param isConnected un Booléen qui dit si l'utilisateur est connecté ou pas
 */
function displayConnected(isConnected) {
    const elementsRequiringConnection = document.getElementsByClassName("requiresConnection");
    const visibilityValue = isConnected ? "visible" : "collapse";
    for(const element of elementsRequiringConnection) {
        element.style.visibility = visibilityValue;
    }
}

window.addEventListener('hashchange', () => { show(window.location.hash); });
// </editor-fold>

// <editor-fold desc="Gestion des requêtes asynchrones">
/**
 * Met à jour le nombre d'utilisateurs de l'API sur la vue "index".
 */
function getNumberOfUsers() {
    const headers = new Headers();
    headers.append("Accept", "application/json");
    const requestConfig = {
        method: "GET",
        headers: headers,
        mode: "cors" // pour le cas où vous utilisez un serveur différent pour l'API et le client.
    };

    fetch(baseUrl + "users", requestConfig)
        .then((response) => {
            if(response.ok && response.headers.get("Content-Type").includes("application/json")) {
                return response.json();
            } else {
                throw new Error("Response is error (" + response.status + ") or does not contain JSON (" + response.headers.get("Content-Type") + ").");
            }
        }).then((json) => {
            if(Array.isArray(json)) {
                document.getElementById("nbUsers").innerText = json.length;
            } else {
                throw new Error(json + " is not an array.");
            }
        }).catch((err) => {
            console.error("In getNumberOfUsers: " + err);
        });
}

/**
 * Envoie la requête de login en fonction du contenu des champs de l'interface.
 */
function connect() {
    displayConnected(true);
    const headers = new Headers();
    headers.append("Content-Type", "application/json");
    const body = {
        login: document.getElementById("login_input").value,
        password: document.getElementById("password_input").value
    };
    const requestConfig = {
        method: "POST",
        headers: headers,
        body: JSON.stringify(body),
        mode: "cors" // pour le cas où vous utilisez un serveur différent pour l'API et le client.
    };
    fetch(baseUrl + "users/login", requestConfig)
        .then(async (response) => {
            if (response.status === 204) {
                console.log("In login: Authorization = " + response.headers.get("Authorization"));
                userAuth.login = body.login;
                userAuth.password = body.password;
                userAuth.authorisation = response.headers.get("Authorization");
                userAuth.isAuthenticated = true;
                displayRequestResult("Connexion réussie", "alert-success");
                displayConnected(true);
                location.hash = "#index";
                userAuth.name = await fetchUserName("users/" + userAuth.login);
                userAuth.todoAssigned = await fetchTodoAssigned(userAuth.login);
                renderTemplate("templateName", userAuth.name, "renderName")
                todosRefresh = setInterval(renderTodos, 5000);
            } else {
                displayRequestResult("Connexion refusée ou impossible", "alert-danger");
                displayConnected(false);
                throw new Error("Bad response code (" + response.status + ").");
            }
        })
        .catch((err) => {
            console.error("In login: " + err);
        })
}

function deco() {
    const headers = new Headers();
    headers.append("Authorization", sessionStorage.getItem('accessToken'));
    headers.append("Content-Type", "application/json");
    const body = {};
    const requestConfig = {
        method: "POST",
        headers: headers,
        body: JSON.stringify(body),
        mode: "cors" // pour le cas où vous utilisez un serveur différent pour l'API et le client.
    };

    fetch(baseUrl + "users/logout", requestConfig)
        .then((response) => {
            if(response.status === 204) {
                userAuth.login = '';
                userAuth.authorisation = '';
                userAuth.isAuthenticated = false;
                displayRequestResult("Déconnexion réussie", "alert-success");
                location.hash = "#index";
                displayConnected(false);
                clearInterval(todosRefresh)
            }
        })
        .catch((err) => {
            console.error("In logout: " + err);
        })
    // TODO envoyer la requête de déconnexion
    location.hash = "#index";
    displayConnected(false);

}
setInterval(getNumberOfUsers, 5000);
// </editor-fold>



async function renderIndex() {
    renderTemplate("templateUsers", null, "renderedUsers");
}

function renderTemplate(templateId, data, targetId) {
    const template = document.getElementById(templateId).innerHTML;
    const render = Mustache.render(template, data);
    document.getElementById(targetId).innerHTML = render;
}

async function renderAccount() {
    userAuth.todoAssigned = await fetchTodoAssigned(userAuth.login);
    const todos = userAuth.todoAssigned.map(todo => [todo.id, todo.title])
    const accountData = {
        login: userAuth.login,
        name: userAuth.name,
        assignedTo: userAuth.todoAssigned
    };
    renderTemplate("templateUser", accountData, "renderUser");

}

async function fetchUserName(id) {
    const headers = new Headers();
    headers.append("Accept", "application/json");
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", userAuth.authorisation);
    const requestConfig = {
        method: "GET",
        headers: headers,
        mode: "cors" // pour le cas où vous utilisez un serveur différent pour l'API et le client.
    };

    let response = await fetch(baseUrl + id + "/name", requestConfig);
    if (response.ok && response.headers.get("Content-Type").includes("application/json")) {
        let data = await response.json();
        return data.name;
    } else {
        throw new Error("Response is error (" + response.status + ") or does not contain JSON (" + response.headers.get("Content-Type") + ").");
    }

}

async function fetchTodoAssigned(id) {
    const headers = new Headers();
    headers.append("Accept", "application/json");
    headers.append("Authorization", userAuth.authorisation);
    const requestConfig = {
        method: "GET",
        headers: headers,
        mode: "cors" // pour le cas où vous utilisez un serveur différent pour l'API et le client.
    };

    let response = await fetch(baseUrl + "users/" + id + "/assignedTodos", requestConfig);
    if (response.ok && response.headers.get("Content-Type").includes("application/json")) {
        let id = await response.json();
        id = id.assignedTodos;
        const todosPromise = id.map(async id => {
            const response = await fetch(baseUrl + id + "/title", requestConfig);
            return await response.json();
        });

        let todos = await Promise.all(todosPromise);
        return todos;

    } else {
        throw new Error("Response is error (" + response.status + ") or does not contain JSON (" + response.headers.get("Content-Type") + ").");
    }

}

function renderName() {
    renderTemplate("templateName", userAuth.name, "renderName")
}



function addTodo(title = '') {
    const body = {
        title: title,
        creator: userAuth.login
    }

    const headers = new Headers();
    headers.append("Accept", "*/*");
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", userAuth.authorisation);
    const requestConfig = {
        method: "POST",
        headers: headers,
        body: JSON.stringify(body),
        mode: "cors" // pour le cas où vous utilisez un serveur différent pour l'API et le client.
    };

    fetch(baseUrl + "todos", requestConfig)
        .then(response => {
            displayRequestResult("Todo ajouté avec succès !", "alert-success");
            renderTodos();
        })
        .catch(e => {
            displayRequestResult("Erreur : " + e + " !", "alert-danger");
        })
}

async function fetchTodos() {
    const headers = new Headers();
    headers.append("Accept", "application/json");
    headers.append("Authorization", userAuth.authorisation);
    const requestConfig = {
        method: "GET",
        headers: headers,
        mode: "cors"
    };

    try {
        const response = await fetch(baseUrl + "todos", requestConfig);
        let ids = await response.json();
        const todosPromise = ids.map(async id => {
            const response = await fetch(baseUrl + "todos/" + id, requestConfig);
            return await response.json();
        });

        const todos = await Promise.all(todosPromise);
        return todos;
    } catch (e) {
        console.error('In fetchTodos() : ' + e);
    }
}
async function renderTodos() {
    const todos = await fetchTodos();
    await Promise.all(todos.map(async todo => {
        todo.assigneeName = todo.assignee === null ? "Non assigné" : await fetchUserName(todo.assignee);
        todo.assigneeLogin = todo.assignee === null ? "Non assigné" : todo.assignee.slice(6);
        todo.toMe = todo.assignee !== null && todo.assignee.slice(6) === userAuth.login;
        todo.special = todo.toMe !== false;
    }));
    const todoData = {
        todos: todos,
    };
    renderTemplate("templateTodoList", todoData, "renderTodoList");
}

function assignUser(hash) {
    const body = {
        assignee: userAuth.login.toString(),
    };
    const headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", userAuth.authorisation);

    const requestConfig = {
        method: "PUT",
        headers: headers,
        mode: "cors",
        body: JSON.stringify(body),
    };

    fetch(baseUrl + "todos/" + hash, requestConfig)
        .then(response => {
            renderTodos();
        })
}

function removeUser(hash) {
    const body = {
        assignee: " ",
    };
    const headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", userAuth.authorisation);

    const requestConfig = {
        method: "PUT",
        headers: headers,
        mode: "cors",
        body: JSON.stringify(body),
    };

    fetch(baseUrl + "todos/" + hash, requestConfig)
        .then(response => {
            renderTodos();
        })
}

function toggleStatus(hash) {
    const body = {
        hash: hash,
    }

    const headers = new Headers();
    headers.append("Accept", "*/*");
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", userAuth.authorisation);
    const requestConfig = {
        method: "POST",
        headers: headers,
        body: JSON.stringify(body),
        mode: "cors" // pour le cas où vous utilisez un serveur différent pour l'API et le client.
    };

    fetch(baseUrl + "todos/toggleStatus", requestConfig)
        .then(response => {
            renderTodos();
        })
}

function removeTodo(hash) {
    const headers = new Headers();
    headers.append("Accept", "*/*");
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", userAuth.authorisation);
    const requestConfig = {
        method: "DELETE",
        headers: headers,
        mode: "cors" // pour le cas où vous utilisez un serveur différent pour l'API et le client.
    };

    fetch(baseUrl + "todos/" + hash, requestConfig)
        .then(response => {
            displayRequestResult("Todo supprimé avec succès !", "alert-success");
            renderTodos();
        })
        .catch(e => {
            displayRequestResult("Erreur : " + e + " !", "alert-danger");
        })
}

function showPopUp(hash) {
    let popup = document.getElementById("popUpName_" + hash);
    if (popup !== null) {
        popup.style.display = "block";
    }
}

function hidePopUp(hash) {
    let popup = document.getElementById("popUpName_" + hash);
    if (popup !== null) {
        popup.style.display = "none";
    }
}

function modifyTitle(title, hash) {

        if(title !== '') {
            const body = {
                title: title
            };
            const headers = new Headers();
            headers.append("Accept", "*/*");
            headers.append("Content-Type", "application/json");
            headers.append("Authorization", userAuth.authorisation);
            const requestConfig = {
                method: "PUT",
                headers: headers,
                body: JSON.stringify(body),
                mode: "cors" // pour le cas où vous utilisez un serveur différent pour l'API et le client.
            };
            fetch(baseUrl + "todos/" + hash, requestConfig)
                .then(response => {
                    displayRequestResult("Todo modifié avec succès !", "alert-success");
                    renderTodos();
                })
                .catch(e => {
                    displayRequestResult("Erreur : " + e + " !", "alert-danger");
                })
        }
    }
