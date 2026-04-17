import os
from typing import Optional

import requests


class ApiClient:
    def __init__(self, base_url: Optional[str] = None, timeout: int = 20) -> None:
        self.base_url = (base_url or os.getenv("BASE_URL", "")).rstrip("/")
        if not self.base_url:
            raise ValueError("BASE_URL is not set")
        self.timeout = timeout

    def get(self, path: str = "") -> requests.Response:
        if path and not path.startswith("/"):
            path = f"/{path}"
        url = f"{self.base_url}{path}"
        return requests.get(url, timeout=self.timeout)