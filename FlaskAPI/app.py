from flask import Flask, request, jsonify
from models import db, Article, Comment
from scripts.populate import populate_database

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///ma_db.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db.init_app(app)

# Route d'accueil avec la documentation de l'API
@app.route('/', methods=['GET'])
def home():
    api_doc = {
        "message": "Notre API:",
        "endpoints": [
            {
                "path": "/articles",
                "methods": ["GET", "POST", "PUT"],
            },
            {
                "path": "/comments",
                "methods": ["GET", "POST", "PUT", "DELETE"],
            }
        ]
    }
    return jsonify(api_doc)

# Endpoint pour récupérer tous les articles (pagination)
@app.route('/articles', methods=['GET'])
def get_articles():
    page = request.args.get('page', 1, type=int)
    articles = Article.query.paginate(page=page, per_page=5)
    return jsonify({
        'data': [{
            'article_id': a.article_id,
            'title': a.title,
            'slug': a.slug,
            'content': a.content,
            'author': a.author
        } for a in articles.items],
        'links': {
            'self': f'/articles?page={page}',
            'next': f'/articles?page={page + 1}' if articles.has_next else None,
            'prev': f'/articles?page={page - 1}' if articles.has_prev else None,
        }
    })

# Endpoint pour rechercher des articles
@app.route('/articles/search', methods=['GET'])
def search_articles():
    content = request.args.get('content', '')
    author = request.args.get('author', '')

    query = Article.query
    if content:
        query = query.filter(Article.content.like(f'%{content}%'))
    if author:
        query = query.filter(Article.author.like(f'%{author}%'))

    articles = query.all()
    return jsonify([{
        'article_id': a.article_id,
        'title': a.title,
        'slug': a.slug,
        'content': a.content,
        'author': a.author
    } for a in articles])

# Endpoint pour récupérer un article spécifique
@app.route('/articles/<int:article_id>', methods=['GET'])
def get_article(article_id):
    article = Article.query.get_or_404(article_id)
    return jsonify({
        'article_id': article.article_id,
        'title': article.title,
        'slug': article.slug,
        'content': article.content,
        'author': article.author
    })

# Endpoint pour créer un article
@app.route('/articles', methods=['POST'])
def create_article():
    data = request.json
    new_article = Article(title=data['title'], slug=data['slug'], content=data['content'], author=data['author'])
    db.session.add(new_article)
    db.session.commit()
    return jsonify({
        'article_id': new_article.article_id,
        'title': new_article.title,
        'slug': new_article.slug,
        'content': new_article.content,
        'author': new_article.author
    }), 201

# Endpoint pour mettre à jour un article
@app.route('/articles/<int:article_id>', methods=['PUT'])
def update_article(article_id):
    article = Article.query.get_or_404(article_id)
    data = request.json
    article.title = data['title']
    article.slug = data['slug']
    article.content = data['content']
    article.author = data['author']
    db.session.commit()
    return jsonify({
        'article_id': article.article_id,
        'title': article.title,
        'slug': article.slug,
        'content': article.content,
        'author': article.author
    })

# Endpoint pour supprimer un article
@app.route('/articles/<int:article_id>', methods=['DELETE'])
def delete_article(article_id):
    article = Article.query.get_or_404(article_id)
    comments = Comment.query.filter_by(article_id=article_id).all()
    for comment in comments:
        db.session.delete(comment)
    db.session.delete(article)
    db.session.commit()
    return '', 204

# Endpoint pour récupérer tous les commentaires
@app.route('/comments', methods=['GET'])
def get_all_comments():
    comments = Comment.query.all()
    return jsonify([{
        'comment_id': c.comment_id,
        'content': c.content,
        'author': c.author,
        'article_id': c.article_id,
        'links': {
            'self': f'/articles/{c.article_id}'
        }
    } for c in comments])

# Endpoint pour récupérer les commentaires d'un article
@app.route('/articles/<int:article_id>/comments', methods=['GET'])
def get_comments(article_id):
    Article.query.get_or_404(article_id)
    comments = Comment.query.filter_by(article_id=article_id).all()
    return jsonify([{
        'comment_id': c.comment_id,
        'content': c.content,
        'author': c.author,
        'article_id': c.article_id
    } for c in comments])

# Endpoint pour récupérer un commentaire spécifique d'un article
@app.route('/articles/<int:article_id>/comments/<int:comment_index>', methods=['GET'])
def get_comment(article_id, comment_index):
    Article.query.get_or_404(article_id)
    comments = Comment.query.filter_by(article_id=article_id).all()
    if comment_index < 0 or comment_index >= len(comments):
        return jsonify({"error": "Comment index out of range"}), 404
    comment = comments[comment_index]
    return jsonify({
        'comment_id': comment.comment_id,
        'content': comment.content,
        'author': comment.author,
        'article_id': comment.article_id
    })

# Endpoint pour créer un commentaire
@app.route('/articles/<int:article_id>/comments', methods=['POST'])
def create_comment(article_id):
    data = request.json
    new_comment = Comment(content=data['content'], author=data['author'], article_id=article_id)
    db.session.add(new_comment)
    db.session.commit()
    return jsonify({
        'comment_id': new_comment.comment_id,
        'content': new_comment.content,
        'author': new_comment.author,
        'article_id': new_comment.article_id
    }), 201

# Endpoint pour supprimer un commentaire
@app.route('/comments/<int:comment_id>', methods=['DELETE'])
def delete_comment(comment_id):
    comment = Comment.query.filter_by(comment_id=comment_id).first_or_404()
    db.session.delete(comment)
    db.session.commit()
    return '', 204

if __name__ == "__main__":
    with app.app_context():
        db.create_all()
        populate_database(10)
    app.run(port=8000)

@app.route('/comments/<int:comment_id>', methods=['PATCH'])
def patch_comment(comment_id):
    comment = Comment.query.get_or_404(comment_id)
    data = request.json
    if 'content' in data:
        comment.content = data['content']
    if 'author' in data:
        comment.author = data['author']
    db.session.commit()
    return jsonify({
        'comment_id': comment.comment_id,
        'content': comment.content,
        'author': comment.author,
        'article_id': comment.article_id
    })

@app.route('/comments/<int:comment_id>', methods=['PUT'])
def put_comment(comment_id):
    comment = Comment.query.get_or_404(comment_id)
    data = request.json
    comment.content = data['content']  
    comment.author = data['author']    
    db.session.commit()
    return jsonify({
        'comment_id': comment.comment_id,
        'content': comment.content,
        'author': comment.author,
        'article_id': comment.article_id
    })

@app.route('/articles/<int:article_id>', methods=['PATCH'])
def patch_article(article_id):
    article = Article.query.get_or_404(article_id)
    data = request.json
    if 'title' in data:
        article.title = data['title']
    if 'slug' in data:
        article.slug = data['slug']
    if 'content' in data:
        article.content = data['content']
    if 'author' in data:
        article.author = data['author']
    db.session.commit()
    return jsonify({
        'article_id': article.article_id,
        'title': article.title,
        'slug': article.slug,
        'content': article.content,
        'author': article.author
    })

@app.route('/articles/<int:article_id>', methods=['HEAD'])
def head_article(article_id):
    article = Article.query.get_or_404(article_id)
    
    response = app.make_response(('', 200))  
    response.headers['X-Article-Exist'] = 'True' 
    response.headers['Last-Modified'] = article.updated_at.strftime('%a, %d %b %Y %H:%M:%S GMT') 
    return response



@app.errorhandler(500)
def internal_server_error(error):
    return jsonify({'error': 'Oops, something went wrong on our side!', 'message': str(error)}), 500

@app.errorhandler(404)
def not_found_error(error):
    return jsonify({'error': 'Sorry, we couldn\'t find what you were looking for!', 'message': str(error)}), 404

@app.errorhandler(405)
def method_not_allowed_error(error):
    return jsonify({'error': 'This method is not allowed for the requested resource.', 'message': str(error)}), 405

