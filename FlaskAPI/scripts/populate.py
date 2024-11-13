import random
from faker import Faker
from models import db, Article, Comment

fake = Faker()

def populate_database(nb_articles):
    
    if Article.query.count() > 0:
        print("Base de données déja populated.")
        return
    
    for _ in range(nb_articles):
        title = fake.sentence(nb_words=5)
        slug = fake.slug()
        content = fake.paragraph(nb_sentences=10)
        author = fake.name()

        article = Article(title=title, slug=slug, content=content, author=author)
        db.session.add(article)
        db.session.commit()

        num_comments = random.randint(2, 10)
        for _ in range(num_comments):
            comment_content = fake.paragraph(nb_sentences=3)
            comment_author = fake.name()
            comment = Comment(content=comment_content, author=comment_author, article_id=article.article_id)
            db.session.add(comment)

    db.session.commit()
    print("Base de données remplie avec des articles et des commentaires d'exemple.")
