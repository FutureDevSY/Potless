from fastapi import Body
from pydantic import BaseModel, Field
from fastapi import File
from typing import List

class Box(BaseModel):
    x: float
    y: float
    width: float
    height: float

class DetectionResponse(BaseModel):
    severity: int = Field(..., alias="severity")
    width: float = Field(..., alias="width")