from pathlib import Path

import pytest
from dotenv import load_dotenv

from src.api.client import ApiClient


load_dotenv(dotenv_path=Path(__file__).resolve().parent.parent / ".env")


@pytest.fixture
def api_client() -> ApiClient:
    return ApiClient()