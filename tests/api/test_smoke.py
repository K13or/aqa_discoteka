import os
import requests


def test_healthcheck():
    base_url = os.getenv("BASE_URL", "https://example.com")
    response = requests.get(base_url, timeout=20)
    assert response.status_code < 500