"use client"
import { useSearchParams } from "next/navigation";
import Cookies from "js-cookie";
import { useQuery } from '@tanstack/react-query';
import jwtDecode from "jwt-decode"; 

export default function Home() {
  const existingToken = Cookies.get("JwtToken");
  const { data: user, error, isLoading } = useQuery(['userInfo'], getUserInfo);
  console.log(existingToken);

async function getUserInfo() {
    try {
      const response = await fetch( "http://localhost:8080/user", {
        headers: {
         // Authorization: "Bearer " + token,
        },
        credentials: "include",
      });
      if (!response.ok) {
        throw new Error(`HTTP Error! Status: ${response.status}`);
      }
     
      const data = await response.json();
      return data;
    } catch (error) {
      throw new Error("Error fetching data: " + error);
    }
  }

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error!</div>;
  }

  return (
    <div>
      <h1>User Information</h1>
      <p>Name: {user?.name}</p>
      <p>Email: {user?.email}</p>
    </div>
  );
}


