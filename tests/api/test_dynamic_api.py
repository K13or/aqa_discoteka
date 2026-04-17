import pytest
from src.api.client import ApiClient
from tests.api.utils import load_test_cases


@pytest.mark.parametrize("case", load_test_cases())
def test_api_cases(case):
    client = ApiClient()

    response = client.get(case["path"])

    assert response.status_code == case["expected_status"]

    if "expected_text" in case:
        assert case["expected_text"] in response.text