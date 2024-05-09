import uvicorn
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from common.config import settings
from routers import detection_router


def get_application():
    _app = FastAPI(title=settings.PROJECT_NAME)

    _app.add_middleware(
        CORSMiddleware,
        allow_origins=[str(origin) for origin in settings.BACKEND_CORS_ORIGINS],
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )

    prefix = "/api"  # Define the prefix

    _app.include_router(detection_router.rec, prefix=prefix)

    return _app


app = get_application()


# @app.on_event("startup")
# async def startup_event():
#     global item_features
#     item_features = ItemFeatures()
#     return


@app.get("/")
async def getTest():
    return {"message": "Test api 호출 완료"}



if __name__ == "__main__":
    uvicorn.run(
        "main:app",
        reload=True,
        reload_dirs=["app/"],
    )