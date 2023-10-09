"use client"
import { useSearchParams } from "next/navigation";
import Cookies from "js-cookie";
import jwtDecode from "jwt-decode"; 
import { useEffect, useState } from "react";

type UserDetails = {
  firstName: string,
  lastName: string,
  email: string,
  profilePicture: string
}

export default function Home() {
  const [userDetails, setUserDetails] = useState<UserDetails>();
  
  useEffect(() => {
    async function fetchData() {
      try {
        const response = await fetch("http://localhost:8080/user", {
          credentials: "include", // send what cookies you have back
        });
  
        if (!response.ok) {
          throw new Error(`HTTP Error! Status: ${response.status}`);
        }
        const data: UserDetails = await response.json();
        console.log(data);
        setUserDetails(data);
      } catch (error) {
        console.error("Error fetching data: " + error);
      }
    }
  
    fetchData(); 
    console.log(userDetails);
  }, []);

  return (
    <div>
    {userDetails ? (
      <div>
        <p>First Name: {userDetails.firstName}</p>
        <p>Last Name: {userDetails.lastName}</p>
        <p>Email: {userDetails.email}</p>
      </div>
    ) : (
      <p>Loading user details...</p>
    )}
  </div>
  );
}
