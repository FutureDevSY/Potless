from fastapi import Body
from pydantic import BaseModel, Field
from fastapi import File
from typing import List

class DetectionResponse(BaseModel):
    severity: int = Field(..., alias="severity")
