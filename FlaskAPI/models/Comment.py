from .db import db

class Comment(db.Model):
    __tablename__ = 'comments'

    comment_id = db.Column(db.Integer, primary_key=True)
    content = db.Column(db.Text, nullable=False)
    author = db.Column(db.String(100), nullable=False)
    article_id = db.Column(db.Integer, db.ForeignKey('articles.article_id'), nullable=False)

    def __repr__(self):
        return f'<Comment {self.content[:20]}>'
