# Api_Rest_Biblioteca

#Endpoints disponíveis  
Autores — /autores → GET, POST, PUT, DELETE /{id}  
Categorias — /categorias → GET, POST, PUT, DELETE /{id}  
Livros — /livros → GET, POST, PUT, DELETE /{id}  
Usuários — /usuarios → GET, POST, PUT, DELETE /{id}  
Empréstimos — /emprestimos  
  
GET /emprestimos — todos  
GET /emprestimos/{id} — por id  
GET /emprestimos/usuario/{id} — todos do usuário  
GET /emprestimos/usuario/{id}/ativos — apenas ativos  
POST /emprestimos — novo empréstimo  
PATCH /emprestimos/{id}/devolver — devolução  
  
  
#Ordem de cadastro no Postman  
Por causa dos relacionamentos, siga essa ordem:  
  
POST /autores → cria um autor  
POST /categorias → cria uma categoria  
POST /livros → cria livro referenciando autor e categoria  
POST /usuarios → cria um usuário  
POST /emprestimos → realiza o empréstimo  
