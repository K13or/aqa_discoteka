import pytest
from tests.api.utils import load_test_cases


@pytest.mark.parametrize(
    "case",
    load_test_cases(),
    ids=lambda case: case["name"]
)
def test_api_cases(api_client, case):
    response = api_client.get(case["path"])

    assert response.status_code == case["expected_status"]

    if "expected_text" in case:
        assert case["expected_text"] in response.text