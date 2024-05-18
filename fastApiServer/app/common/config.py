from typing import List, Union
import logging

from pydantic import AnyHttpUrl, validator
from pydantic_settings import BaseSettings

from dotenv import load_dotenv
import os

load_dotenv()

# AWS_ACCESS_KEY_ID=your_access_key_id
# AWS_SECRET_ACCESS_KEY=your_secret_access_key
# AWS_DEFAULT_REGION=your_region

class Settings(BaseSettings):
    PROJECT_NAME: str
    BACKEND_CORS_ORIGINS: List[AnyHttpUrl] = []
    MODEL_PATH: str
    MODEL_NAME: str
    DATASET_PATH: str
    DATASET_NAME: str
    ENCODING: str
    LOG_LEVEL: str
    SPRING_BASE_URL: str
    AWS_ACCESS_KEY_ID: str
    AWS_SECRET_ACCESS_KEY: str
    AWS_DEFAULT_REGION: str

    @validator("BACKEND_CORS_ORIGINS", pre=True)
    def assemble_cors_origins(cls, v: Union[str, List[str]]) -> Union[List[str], str]:
        if isinstance(v, str) and not v.startswith("["):
            return [i.strip() for i in v.split(",")]
        elif isinstance(v, (list, str)):
            return v
        raise ValueError(v)

    class Config:
        case_sensitive = True
        env_file = ".env"


class Settings(Settings):
    class Config:
        env_file = ".env"


# class ProdSettings(Settings):
#     class Config:
#         env_file = ".env.prod"


def get_settings() :
    # if os.getenv("ENV") == "PROD":
    #     return ProdSettings()
    return Settings()

# def get_settings() -> Union[DevSettings, ProdSettings]:
#      if os.getenv("ENV") == "PROD":
#          return ProdSettings()
#     return DevSettings()


settings = get_settings()

logging.basicConfig(
    level=settings.LOG_LEVEL,  # 로그 수준
    format="%(asctime)s %(levelname)s %(message)s",
    handlers=[logging.StreamHandler()],
)

logging.info(settings)