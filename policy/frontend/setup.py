from setuptools import setup, find_packages

setup(
    name="openbot_frontend",
    version="0.6.0",
    description="OpenBot Frontend package",
    url="https://github.com/sanyatuning/OpenBot",
    author="Balazs Sandor",
    author_email="sanyatuning@gmail.com",
    license="MIT",
    packages=find_packages(include=["openbot_frontend", "openbot_frontend.*"]),
    include_package_data=True,
    zip_safe=False,
)
