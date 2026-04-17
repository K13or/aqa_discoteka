from src.api.client import ApiClient


def test_homepage_contains_expected_text():
    client = ApiClient()
    response = client.get()

    assert response.status_code == 200
    assert "Example Domain" in response.text