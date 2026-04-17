from src.api.client import ApiClient


def test_base_url_is_reachable():
    client = ApiClient()
    response = client.get()
    assert response.status_code < 500