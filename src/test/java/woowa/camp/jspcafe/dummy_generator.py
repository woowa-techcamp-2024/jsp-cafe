import csv
import random
from datetime import datetime, timedelta
import os

# 상수 정의
USER_COUNT = 10_000
ARTICLE_COUNT = 10_000_000
MAX_REPLIES_PER_ARTICLE = 5

# CSV 파일이 저장될 디렉토리 경로 설정
OUTPUT_DIR = '../dummy_csv'

# 디렉토리가 없으면 생성
os.makedirs(OUTPUT_DIR, exist_ok=True)

# 시작 날짜 설정 (예: 1년 전)
START_DATE = datetime.now() - timedelta(days=365)

def generate_users():
    print('유저 더미 데이터 생성 시작')
    with open(os.path.join(OUTPUT_DIR, 'users.csv'), 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['email', 'nickname', 'password', 'register_at'])
        for i in range(1, USER_COUNT + 1):
            register_date = START_DATE + timedelta(minutes=i)  # 각 사용자마다 1분씩 증가
            writer.writerow([
                f'user{i}@example.com',
                f'user{i}',
                f'password{i}',
                register_date.strftime('%Y-%m-%d %H:%M:%S')
            ])
    print('유저 더미 데이터 생성 종료')

def generate_articles():
    print('게시글 더미 데이터 생성 시작')
    with open(os.path.join(OUTPUT_DIR, 'articles.csv'), 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['author_id', 'title', 'content', 'hits', 'created_at', 'updated_at'])
        for i in range(1, ARTICLE_COUNT + 1):
            created_at = START_DATE + timedelta(seconds=i)  # 각 게시글마다 1초씩 증가
            writer.writerow([
                random.randint(1, USER_COUNT),
                f'Title {i}',
                f'Content {i}',
                random.randint(0, 1000),
                created_at.strftime('%Y-%m-%d %H:%M:%S'),
                created_at.strftime('%Y-%m-%d %H:%M:%S')  # updated_at은 created_at과 동일
            ])
    print('게시글 더미 데이터 생성 종료')

def get_random_reply_count():
    probabilities = [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.05, 0.05]
    return random.choices(range(11), weights=probabilities)[0]

def generate_replies():
    print('댓글 더미 데이터 생성 시작')
    with open(os.path.join(OUTPUT_DIR, 'replies.csv'), 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['user_id', 'article_id', 'content', 'created_at', 'updated_at'])
        reply_id = 1
        total_replies = 0
        for article_id in range(1, ARTICLE_COUNT + 1):
            reply_count = get_random_reply_count()
            total_replies += reply_count
            for j in range(reply_count):
                created_at = START_DATE + timedelta(seconds=ARTICLE_COUNT + reply_id)  # 게시글 이후로 시작
                writer.writerow([
                    random.randint(1, USER_COUNT),
                    article_id,
                    f'Reply content {reply_id}',
                    created_at.strftime('%Y-%m-%d %H:%M:%S'),
                    created_at.strftime('%Y-%m-%d %H:%M:%S')  # updated_at은 created_at과 동일
                ])
                reply_id += 1
        print(f"Total replies: {total_replies}, Average replies per article: {total_replies / ARTICLE_COUNT:.2f}")

    print('댓글 더미 데이터 생성 종료')


if __name__ == '__main__':
    generate_users()
    generate_articles()
    generate_replies()