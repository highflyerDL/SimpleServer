from locust import HttpLocust, TaskSet, task
import random

class UserBehavior(TaskSet):
    @task(1)
    def profile(self):
        randomId = random.randint(1, 1000)
        self.client.get("/accounts/{}".format(randomId))

class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    min_wait = 5000
    max_wait = 9000

